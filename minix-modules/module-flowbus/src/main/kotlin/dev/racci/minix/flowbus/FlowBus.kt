package dev.racci.minix.flowbus

import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.reflection.safeCast
import dev.racci.minix.api.utils.kotlin.ifFalse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.apiguardian.api.API
import org.koin.core.annotation.Singleton
import kotlin.reflect.KClass

/**
 * This class holds all state flows and handles event posting.
 * You can use the Koin singleton that is just plain instance of this class or create your own implementation.
 */
@Singleton
@API(status = API.Status.EXPERIMENTAL, since = "5.0.0")
public open class FlowBus {
    protected open val flows: MutableMap<KClass<*>, RendezvousStateFlow<*>> = mutableMapOf()

    /**
     * Gets a Flow for events of the given type.
     *
     * **This flow never completes.**
     *
     * The returned Flow is _hot_ as it is based on a [SharedFlow]. This means a call to [collect] never completes normally, calling [toList] will suspend forever, etc.
     *
     * You are entirely responsible to cancel this flow. To cancel this flow, the scope in which the coroutine is running needs to be cancelled.
     * @see [SharedFlow]
     */
    public fun <T : Any> getFlow(clazz: KClass<T>): Flow<T> {
        return forEvent(clazz).asStateFlow().filterNotNull()
    }

    public inline fun <reified T : Any> postLazy(
        retain: Boolean = false,
        lazyEvent: () -> T
    ) {
        val flow = this.forEvent(T::class)
        if (!retain && flow.subscriptionCount.value == 0) return // Don't bother if no one is listening and we don't want to retain.

        `access$postOn`(flow, lazyEvent(), retain)
    }

    /**
     * Posts a new event to StateFlow of the [event] type.
     * @param retain If the [event] should be retained in the flow for future subscribers. This is false by default.
     */
    public fun <T : Any> post(
        event: T,
        retain: Boolean = false
    ) {
        val flow = forEvent(event::class)

        postOn(flow, event, retain)
    }

    public suspend fun <T : Any> postDeferred(
        event: T,
        retain: Boolean = false
    ): Deferred<Unit> {
        val flow = forEvent(event::class)

        return postOnDeferred(flow, event, retain)
    }

    /**
     * Returns last posted event that was instance of [clazz] or `null` if no event of the given type is retained.
     * @return A retained event that is instance of [clazz]
     */
    public fun <T : Any> getLastEvent(clazz: KClass<T>): T? {
        return flows.getOrElse(clazz) { null }?.value.safeCast()
    }

    /** Removes retained event of type [clazz] */
    public fun <T : Any> dropEvent(clazz: KClass<T>) {
        if (!flows.contains(clazz)) return
        val channel = flows[clazz].castOrThrow<MutableStateFlow<T?>>()
        channel.tryEmit(null)
    }

    /** Removes all retained events */
    public fun dropAll() {
        flows.values.forEach { flow ->
            flow.castOrThrow<MutableStateFlow<Any?>>().tryEmit(null)
        }
    }

    @API(status = API.Status.INTERNAL)
    public open fun <T : Any> forEvent(clazz: KClass<out T>): RendezvousStateFlow<T?> {
        return flows.getOrPut(clazz) { RendezvousStateFlow<T?>(null) }.castOrThrow()
    }

    protected open fun <T : Any> postOn(
        flow: RendezvousStateFlow<T?>,
        event: T,
        retain: Boolean
    ) {
        flow.tryEmit(event).ifFalse { error("StateFlow cannot take element. How did this happen??") }

        if (retain) return

        // Without starting a coroutine here, the event is dropped immediately,
        // and not delivered to subscribers.
        CoroutineScope(Job() + Dispatchers.Unconfined).launch {
            dropEvent(event::class)
        }
    }

    protected open suspend fun <T : Any> postOnDeferred(
        flow: RendezvousStateFlow<T?>,
        event: T,
        retain: Boolean
    ): Deferred<Unit> {
        val deferred = flow.emitAsync(event)
        if (retain) return deferred

        // Without starting a coroutine here, the event is dropped immediately,
        // and not delivered to subscribers.
        CoroutineScope(Job() + Dispatchers.Unconfined).launch {
            dropEvent(event::class)
        }

        return deferred
    }

    @PublishedApi
    internal fun <T : Any> `access$postOn`(flow: RendezvousStateFlow<T?>, event: T, retain: Boolean): Unit = postOn(flow, event, retain)
}
