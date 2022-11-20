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

// @ExperimentalCoroutinesApi
// internal class PriorityChannel<T>(
//    private val maxCapacity: Int = 4096,
//    scope: CoroutineScope = GlobalScope,
//    comparator: Comparator<T>
// ) : ProcessChannel<T>(
//    // why a rendezvous channel should be the input channel?
//    // because we buffer and sort the messages in the co-routine
//    // that is where the capacity constraint is enforced
//    // and the buffer we keep sorted, the input channel we can't
//    inChannel = Channel(RENDEZVOUS),
//    // output channel is rendezvous channel because we may still
//    // get higher priority input meanwhile and we will send that
//    // when output consumer is ready to take it
//    outChannel = Channel(RENDEZVOUS)
// ) {
//    private val buffer = PriorityQueue(comparator)
//
//    private fun PriorityQueue<T>.isNotFull() = this.size < maxCapacity
//
//    private fun PriorityQueue<T>.isFull() = this.size >= maxCapacity
//
//    // non-suspending way to get all messages available at the moment
//    // as long as we have anything to receive and the buffer is not full
//    // we should keep receiving
//    private fun tryGetSome() {
//        if (buffer.isNotFull()) {
//            var received = inChannel.tryReceive().getOrNull()
//            if (received != null) {
//                buffer.add(received)
//                while (buffer.isNotFull() && received != null) {
//                    received = inChannel.tryReceive().getOrNull()
//                    received?.let { buffer.add(it) }
//                }
//            }
//        }
//    }
//
//    private suspend fun getAtLeastOne() {
//        buffer.add(inChannel.receive())
//        tryGetSome()
//    }
//
//    private suspend fun trySendSome() {
//        when {
//            buffer.isEmpty() -> {
//                yield()
//            }
//            buffer.isFull() -> {
//                outChannel.send(buffer.poll())
//            }
//            else -> {
//                while (buffer.isNotEmpty() && outChannel.trySend(buffer.peek()).isSuccess) {
//                    buffer.poll()
//                    tryGetSome()
//                }
//            }
//        }
//    }
//
//    private suspend fun sendAll() {
//        while (buffer.isNotEmpty()) {
//            outChannel.send(buffer.poll())
//        }
//    }
//
//    init {
//        require(maxCapacity >= 2) {
//            "priorityChannel maxCapacity < 2 does not make any sense"
//        }
//
//        scope.async {
//            try {
//                getAtLeastOne()
//
//                while (!inChannel.isClosedForReceive) {
//                    trySendSome()
//                    tryGetSome()
//                }
//            } finally {
//                // input channel closed, send the buffer to out channel
//                sendAll()
//                // and finally close the output channel, signaling that that this was it
//                outChannel.close()
//            }
//        }.start()
//    }
// }
