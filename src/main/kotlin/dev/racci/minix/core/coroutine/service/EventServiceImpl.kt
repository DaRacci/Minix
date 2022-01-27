package dev.racci.minix.core.coroutine.service

import dev.racci.minix.api.annotations.RunAsync
import dev.racci.minix.api.coroutine.contract.CoroutineSession
import dev.racci.minix.api.coroutine.contract.EventService
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.coroutine.extension.invokeSuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.bukkit.Warning
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventException
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.AuthorNagException
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredListener
import org.bukkit.plugin.SimplePluginManager
import java.lang.Deprecated
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

internal class EventServiceImpl(
    private val plugin: MinixPlugin,
    private val coroutineSession: CoroutineSession,
) : EventService {

    /**
     * Registers a suspend listener.
     */
    override fun registerSuspendListener(listener: Listener) {
        val registeredListeners = createCoroutineListener(listener, plugin)

        val method = SimplePluginManager::class.java
            .getDeclaredMethod("getEventListeners", Class::class.java)
        method.isAccessible = true

        for (entry in registeredListeners.entries) {
            val clazz = entry.key
            val handlerList = method.invoke(plugin.server.pluginManager, clazz) as HandlerList
            handlerList.registerAll(entry.value as MutableCollection<RegisteredListener>)
        }
    }

    /**
     * Fires a suspending event.
     */
    @Suppress("TooGenericExceptionCaught")
    override fun fireSuspendingEvent(event: Event): Collection<Job> {
        val listeners = event.handlers.registeredListeners
        val jobs = ArrayList<Job>()

        for (registration in listeners) {
            if (!registration.plugin.isEnabled) continue

            try {
                if (registration is SuspendingRegisteredListener) {
                    val job = registration.callSuspendingEvent(event)
                    jobs.add(job)
                } else {
                    registration.callEvent(event)
                }
            } catch (e: Throwable) {
                plugin.log.error(e) {
                    "Could not pass event ${event.eventName} to ${registration.plugin.description.fullName}"
                }
            }
        }

        return jobs
    }

    private fun createCoroutineListener(
        listener: Listener,
        plugin: MinixPlugin,
    ): Map<Class<*>, MutableSet<RegisteredListener>> {
        val eventMethods = HashSet<Method>()

        try {
            // Adds public methods of the current class and inherited classes
            eventMethods.addAll(listener.javaClass.methods)
            // Adds all methods of the current class
            eventMethods.addAll(listener.javaClass.declaredMethods)
        } catch (e: NoClassDefFoundError) {
            plugin.log.error(e) {
                "Plugin ${plugin.description.fullName} has failed to register events " +
                    "for ${listener.javaClass} because ${e.message} does not exist."
            }
            return emptyMap()
        }

        val result = mutableMapOf<Class<*>, MutableSet<RegisteredListener>>()

        for (method in eventMethods) {
            val annotation = method.getAnnotation(EventHandler::class.java)

            if (annotation == null || method.isBridge || method.isSynthetic) {
                continue
            }

            val eventClass = method.parameterTypes[0].asSubclass(Event::class.java)
            method.isAccessible = true

            if (!result.containsKey(eventClass)) {
                result[eventClass] = HashSet()
            }

            var clazz: Class<*> = eventClass

            while (Event::class.java.isAssignableFrom(clazz)) {
                if (clazz.getAnnotation(Deprecated::class.java) == null) {
                    clazz = clazz.superclass
                    continue
                }

                val warning = clazz.getAnnotation(Warning::class.java)
                val warningState = plugin.server.warningState

                if (!warningState.printFor(warning)) {
                    break
                }

                plugin.log.warn(
                    if (warningState == Warning.WarningState.ON) {
                        AuthorNagException(null as String?)
                    } else null
                ) {
                    val warn = if (warning?.reason?.isNotEmpty() == true) warning.reason else "Server performance will be affected"
                    "${plugin.description.fullName} has registered a listener for ${clazz.name} on method ${method.toGenericString()}," +
                        "but the event is Deprecated. $warn; please notify the authors ${plugin.description.authors}"
                }
            }

            val executor = SuspendingEventExecutor(eventClass, method, coroutineSession)
            result[eventClass]!!.add(
                SuspendingRegisteredListener(
                    listener,
                    executor,
                    annotation.priority,
                    plugin,
                    annotation.ignoreCancelled
                )
            )
        }

        return result
    }

    class SuspendingEventExecutor(
        private val eventClass: Class<*>,
        private val method: Method,
        private val coroutineSession: CoroutineSession,
    ) : EventExecutor {

        fun executeSuspend(
            listener: Listener,
            event: Event,
        ): Job = executeEvent(listener, event)

        override fun execute(
            listener: Listener,
            event: Event,
        ) {
            executeEvent(listener, event)
        }

        private fun executeEvent(
            listener: Listener,
            event: Event,
        ): Job {
            try {
                if (eventClass.isAssignableFrom(event.javaClass)) {
                    val isAsync = event.isAsynchronous

                    val dispatcher = if (isAsync || listener::class.java.getAnnotation(RunAsync::class.java) != null) {
                        // Unconfined because async events should be supported too.
                        Dispatchers.Unconfined
                    } else {
                        coroutineSession.dispatcherMinecraft
                    }

                    @Suppress("SwallowedException")
                    return coroutineSession.launch(dispatcher) {
                        try {
                            // Try as suspension function.
                            method.invokeSuspend(listener, event)
                        } catch (e: IllegalArgumentException) {
                            // Try as ordinary function.
                            method.invoke(listener, event)
                        }
                    }
                }
            } catch (var4: InvocationTargetException) {
                throw EventException(var4.cause)
            } catch (var5: Throwable) {
                throw EventException(var5)
            }
            return Job()
        }
    }

    class SuspendingRegisteredListener(
        lister: Listener,
        private val executor: EventExecutor,
        priority: EventPriority,
        plugin: Plugin,
        ignoreCancelled: Boolean,
    ) : RegisteredListener(lister, executor, priority, plugin, ignoreCancelled) {

        fun callSuspendingEvent(event: Event): Job {
            if (event is Cancellable &&
                (event as Cancellable).isCancelled &&
                isIgnoringCancelled
            ) return Job()

            return if (executor is SuspendingEventExecutor) {
                executor.executeSuspend(listener, event)
            } else {
                executor.execute(listener, event)
                Job()
            }
        }
    }
}
