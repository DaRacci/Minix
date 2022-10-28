package dev.racci.minix.core.coroutine.dispatcher.service

import arrow.core.filterIsInstance
import dev.racci.minix.api.annotations.RunAsync
import dev.racci.minix.api.coroutine.CoroutineSession
import dev.racci.minix.api.data.enums.EventExecutionType
import dev.racci.minix.api.extensions.collections.findKFunction
import dev.racci.minix.api.extensions.pluginManager
import dev.racci.minix.api.extensions.reflection.accessInvoke
import dev.racci.minix.api.extensions.reflection.safeCast
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.collections.muiltimap.MutableMultiMap
import dev.racci.minix.api.utils.collections.multiMapOf
import dev.racci.minix.api.utils.getKoin
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
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction2
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.javaMethod

internal class EventService(
    private val plugin: MinixPlugin,
    private val coroutineSession: CoroutineSession
) {
    suspend fun registerSuspendListener(listener: Listener) {
        val func = SimplePluginManager::class.declaredMemberFunctions.findKFunction<HandlerList>("getEventListeners")
            .filterIsInstance<KFunction2<SimplePluginManager, Class<in Event>, HandlerList>>()
            .orNull() ?: error("Could not find getEventListeners method in SimplePluginManager")

        createCoroutineListener(listener, plugin).entries.forEach { (eventClass, listeners) -> func.accessInvoke(pluginManager, eventClass).registerAll(listeners) }
    }

    fun fireSuspendingEvent(event: Event): Collection<Job> {
        fun filteredListeners() = event.handlers.registeredListeners.asSequence().filter { reg -> reg.plugin.isEnabled }

        fun Result<*>.handleError(
            reg: RegisteredListener,
            event: Event
        ) = this.onFailure { err ->
            plugin.log.error(err) { "Couldn't pass event ${event.eventName} to ${reg.plugin.description.fullName}" }
        }

        fun fireConcurrent() = filteredListeners().map { reg ->
            runCatching {
                if (reg is SuspendingEventExecutor.SuspendingRegisteredListener) {
                    reg.callSuspendingEvent(event)
                } else reg.callEvent(event)
            }.handleError(reg, event)
        }.filterIsInstance<Job>().toSet()

        fun fireSequential() = plugin.launch(Dispatchers.Unconfined) {
            filteredListeners().forEach { reg ->
                runCatching {
                    if (reg is SuspendingEventExecutor.SuspendingRegisteredListener) {
                        reg.callSuspendingEvent(event).join()
                    } else reg.callEvent(event)
                }.handleError(reg, event)
            }
        }

        return when (/*executionType*/EventExecutionType.Concurrent) {
            EventExecutionType.Concurrent -> fireConcurrent()
            EventExecutionType.Sequential -> listOf(fireSequential())
        }
    }

    private fun createCoroutineListener(
        listener: Listener,
        plugin: MinixPlugin
    ): MutableMultiMap<KClass<*>, RegisteredListener> {
        val result = multiMapOf<KClass<*>, RegisteredListener>()

        plugin.log.debug { "Creating coroutine listener for ${listener::class.simpleName}" }

        runCatching {
            listener::class.functions + listener::class.declaredFunctions
        }.onFailure { err ->
            if (err !is NoClassDefFoundError) throw err
            plugin.log.error(err) { "Failed to register events for ${listener::class} because ${err.message} doesn't exist." }
            return multiMapOf()
        }.getOrThrow().asSequence().filter { func ->
            func.hasAnnotation<EventHandler>()
        }.filter { func -> // TODO -> Is there a Kotlin Reflect method for this?
            func.javaMethod?.isBridge == false && func.javaMethod?.isSynthetic == false
        }.associateWith(::getEventKClass).onEach { (func, eventClass) ->
            var kClass: KClass<*> = eventClass

            while (Event::class.isSuperclassOf(kClass)) {
                if (!kClass.hasAnnotation<Deprecated>()) {
                    kClass = kClass.superclasses.first()
                    continue
                }

                val warning = kClass.findAnnotation<Warning>()
                val state = plugin.server.warningState

                if (!state.printFor(warning)) {
                    break
                }

                val err = if (state == Warning.WarningState.ON) {
                    AuthorNagException(null)
                } else null

                plugin.log.warn(err) { "${plugin.description.fullName} has registered a listener for ${kClass.qualifiedName}, but the event id Deprecated. $func; please notify the authors ${plugin.description.authors}." }
            }
        }.forEach { (func, eventKClass) ->
            val executor = SuspendingEventExecutor(eventKClass, func, coroutineSession)
            val annotation = func.findAnnotation<EventHandler>()!!
            result.put(eventKClass, SuspendingEventExecutor.SuspendingRegisteredListener(listener, executor, annotation.priority, plugin, annotation.ignoreCancelled))
        }

        return result
    }

    private fun getEventKClass(function: KFunction<*>) = function.parameters
        .filter { it.kind == KParameter.Kind.EXTENSION_RECEIVER || it.kind == KParameter.Kind.VALUE }
        .takeUnless { it.isEmpty() || it.size > 1 || !it[0].type.isSubtypeOf(Event::class.starProjectedType) }
        .orEmpty().firstOrNull()?.type?.classifier.safeCast<KClass<Event>>() ?: error("Event function must have exactly one parameter of type *Event. (function: $function)")

    class SuspendingEventExecutor(
        private val eventKClass: KClass<*>,
        private val function: KFunction<*>,
        private val coroutineSession: CoroutineSession
    ) : EventExecutor {

        fun executeSuspend(
            listener: Listener,
            event: Event
        ): Job = executeEvent(listener, event)

        override fun execute(
            listener: Listener,
            event: Event
        ) {
            executeEvent(listener, event)
        }

        private fun executeEvent(
            listener: Listener,
            event: Event
        ): Job {
            if (!eventKClass.isSubclassOf(event::class)) {
                println("Event $eventKClass is not a subclass of ${event::class}")
                return Job()
            }

            val dispatcher = when {
                event.isAsynchronous || listener::class.hasAnnotation<RunAsync>() -> coroutineSession.context
                else -> coroutineSession.minecraftContext
            }

            return runCatching {
                coroutineSession.launch(dispatcher) {
                    println("Calling ${function.name} for $event")
                    function.callSuspend(listener, event)
                }
            }.onFailure { err ->
                val cause = if (err is InvocationTargetException) err.cause else err
                val exception = EventException(cause)
                getKoin().get<MinixLogger>().error(exception) { "Could not pass event ${event.eventName} to ${listener.javaClass.name}" }

                // Empty stacktrace to avoid spamming the console
                exception.stackTrace = emptyArray()
                throw exception
            }.getOrDefault(Job())
        }

        class SuspendingRegisteredListener(
            lister: Listener,
            private val executor: EventExecutor,
            priority: EventPriority,
            plugin: Plugin,
            ignoreCancelled: Boolean
        ) : RegisteredListener(lister, executor, priority, plugin, ignoreCancelled) {

            fun callSuspendingEvent(event: Event): Job {
                when {
                    event.ignoreEvent() -> plugin.logger.info("Event ${event.eventName} was cancelled by a plugin.")
                    executor is SuspendingEventExecutor -> return executor.executeSuspend(listener, event)
                    else -> executor.execute(listener, event)
                }

                return Job()
            }

            private fun Event.ignoreEvent() = this is Cancellable && this.isCancelled && isIgnoringCancelled
        }
    }
}
