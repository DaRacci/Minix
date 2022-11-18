package dev.racci.minix.flowbus.receiver

import dev.racci.minix.api.data.Priority
import dev.racci.minix.flowbus.EmitterCancellable
import dev.racci.minix.flowbus.EventCallback
import dev.racci.minix.flowbus.FlowBus
import dev.racci.minix.flowbus.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNot
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

    override val EventReceiver.exceptionHandler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
    }

    override val EventReceiver.supervisorScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + exceptionHandler + returnDispatcher)
    }

    override fun returnOn(dispatcher: CoroutineDispatcher): EventReceiver {
        returnDispatcher = dispatcher
        return this@EventReceiverImpl
    }

    override fun isCancelled(event: Any): Boolean {
        return event is EmitterCancellable && event.cancelled
    }

    override fun EventReceiver.createEventScope(): CoroutineScope = CoroutineScope(supervisorScope as Job + exceptionHandler)

    override fun <T : Any> EventReceiver.subscribeTo(
        clazz: KClass<T>,
        priority: Priority,
        ignoreCancelled: Boolean,
        skipRetained: Boolean,
        callback: suspend T.() -> Unit
    ): EventReceiver = this@EventReceiverImpl.subscribeTo(clazz, EventCallback(priority, ignoreCancelled, skipRetained, callback))

    override fun <T : Any> EventReceiver.subscribeTo(
        clazz: KClass<T>,
        callback: EventCallback<T>
    ): EventReceiver {
        if (jobs.containsKey(clazz)) {
            throw IllegalArgumentException("Already subscribed for event type: $clazz")
        }

        jobs[clazz] = this@EventReceiverImpl.flowOf(clazz, callback.priority, callback.ignoreCancelled, callback.skipRetained)
            .onEach(callback.callback)
            .flowOn(returnDispatcher)
            .launchIn(createEventScope())

        return this@EventReceiverImpl
    }

    override fun <T : Any> EventReceiver.flowOf(
        clazz: KClass<T>,
        priority: Priority,
        ignoreCancelled: Boolean,
        skipRetained: Boolean
    ): Flow<T> = bus.forEvent(clazz)
        .drop(if (skipRetained) 1 else 0)
        .filterNotNull()
        .filterNot { ignoreCancelled && isCancelled(it) }

    override fun <T : Any> EventReceiver.unsubscribe(clazz: KClass<T>) {
        jobs.remove(clazz)?.cancel()
    }

    override fun EventReceiver.unsubscribe() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
    }
}
