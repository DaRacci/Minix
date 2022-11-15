package dev.racci.minix.flowbus.receiver

import dev.racci.minix.api.data.Priority
import dev.racci.minix.flowbus.dispatcher.DispatcherProvider
import dev.racci.minix.flowbus.EmitterCancellable
import dev.racci.minix.flowbus.EventCallback
import dev.racci.minix.flowbus.FlowBus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.InjectedParam
import kotlin.reflect.KClass

/**
* Class for receiving events posted to [FlowBus]
* @param bus [FlowBus] instance to subscribe to. If not set, the koin singleton will be used.
*/
// TODO -> Cancelable, Priority, Bukkit listener conversion
public open class EventReceiverImpl(@InjectedParam private val bus: FlowBus) : EventReceiver {
    private val jobs = mutableMapOf<KClass<*>, Job>()

    private var returnDispatcher: CoroutineDispatcher = DispatcherProvider.get()

    protected open val exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
    }

    protected open val supervisorScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + exceptionHandler + returnDispatcher)
    }

    /**
     * Set the `CoroutineDispatcher` which will be used to launch your callbacks.
     *
     * If this [EventReceiver] was created on the main thread the default dispatcher will be [Dispatchers.Main].
     * In any other case [Dispatchers.Default] will be used.
     */
    override fun returnOn(dispatcher: CoroutineDispatcher): EventReceiver {
        returnDispatcher = dispatcher
        return this
    }

    override fun isCancelled(event: Any): Boolean {
        return event is EmitterCancellable && event.cancelled
    }

    override fun createScope(): CoroutineScope = CoroutineScope(supervisorScope as Job + exceptionHandler)

    /**
     * Subscribe to events that are type of [clazz] with the given [callback] function.
     * The [callback] can be called immediately if event of type [clazz] is present in the flow.
     *
     * @param clazz Type of event to subscribe to
     * @param skipRetained Skips event already present in the flow. This is `false` by default
     * @param callback The callback function
     * @return This instance of [EventReceiver] for chaining
     */
    override fun <T : Any> subscribeTo(
        clazz: KClass<T>,
        skipRetained: Boolean,
        priority: Priority,
        ignoreCancelled: Boolean,
        callback: suspend (T) -> Unit
    ): EventReceiver {
        if (jobs.containsKey(clazz)) {
            throw IllegalArgumentException("Already subscribed for event type: $clazz")
        }

        jobs[clazz] = flowOf(clazz, skipRetained, priority, ignoreCancelled)
            .onEach(callback)
            .flowOn(returnDispatcher)
            .launchIn(createScope())

        return this
    }

    override fun <T : Any> flowOf(
        clazz: KClass<T>,
        skipRetained: Boolean,
        priority: Priority,
        ignoreCancelled: Boolean
    ): Flow<T> = bus.forEvent(clazz)
        .drop(if (skipRetained) 1 else 0)
        .filterNotNull()
        .filter { !ignoreCancelled || !isCancelled(it) }

    /**
     * A variant of [subscribeTo] that uses an instance of [EventCallback] as callback.
     *
     * @param clazz Type of event to subscribe to
     * @param skipRetained Skips event already present in the flow. This is `false` by default
     * @param callback Interface with implemented callback function
     * @return This instance of [EventReceiver] for chaining
     * @see [subscribeTo]
     */
    override fun <T : Any> subscribeTo(
        clazz: KClass<T>,
        skipRetained: Boolean,
        priority: Priority,
        ignoreCancelled: Boolean,
        callback: EventCallback<T>
    ): EventReceiver = subscribeTo(clazz, skipRetained) { callback.onEvent(it) }

    /**
     * Unsubscribe from events type of [clazz]
     */
    override fun <T : Any> unsubscribe(clazz: KClass<T>) {
        jobs.remove(clazz)?.cancel()
    }

    /**
     * Unsubscribe from all events
     */
    override fun unsubscribe() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
    }
}
