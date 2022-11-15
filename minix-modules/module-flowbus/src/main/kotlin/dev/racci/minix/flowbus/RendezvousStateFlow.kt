package dev.racci.minix.flowbus

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** A simple wrapper for waiting for an event to be fully processed by the listeners. */
public open class RendezvousStateFlow<T> constructor(
    initialValue: T
) : MutableStateFlow<T> {
    private val backingFlow: MutableStateFlow<T> = MutableStateFlow(initialValue)
    private val atomicHolders = atomic(0)
    private val rendezvous = Channel<Unit>()

    override val replayCache: List<T> by backingFlow::replayCache
    override val subscriptionCount: StateFlow<Int> by backingFlow::subscriptionCount
    override var value: T by backingFlow::value

    override fun compareAndSet(expect: T, update: T): Boolean = backingFlow.compareAndSet(expect, update)

    override suspend fun emit(value: T) {
        backingFlow.emit(value)

        repeat(atomicHolders.value) {
            rendezvous.receive()
        }
    }

    override fun tryEmit(value: T): Boolean = backingFlow.tryEmit(value)

    override suspend fun collect(collector: FlowCollector<T>): Nothing = backingFlow.collect(collector)

    @ExperimentalCoroutinesApi
    override fun resetReplayCache(): Unit = backingFlow.resetReplayCache()

    /** Returns a deferred wrapper for the [emit] function. */
    @OptIn(DelicateCoroutinesApi::class)
    public fun emitAsync(value: T): Deferred<Unit> {
        return GlobalScope.async {
            emit(value)
        }
    }

    /** Allows the posting event to await the result of this listener. */
    public suspend fun collectHolding(collector: suspend (T) -> Unit) {
        atomicHolders.incrementAndGet()
        backingFlow.collect {
            collector(it)
            rendezvous.send(Unit)
        }
    }
}
