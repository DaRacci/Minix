package dev.racci.minix.api.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Semaphore
import org.apiguardian.api.API
import kotlin.math.min

@OptIn(ExperimentalCoroutinesApi::class)
@API(status = API.Status.EXPERIMENTAL, since = "4.0.0")
private fun <T, R> Flow<T>.pipelinedMap(
    bufferSize: Int,
    concurrency: Int,
    block: suspend (T) -> R
): Flow<R> {
    require(bufferSize > 0) { "Not sure what you want to happen" }
    require(concurrency > 1) { "Why even bother?" }

    return flow {
        coroutineScope {
            val buffer = produce(capacity = bufferSize) {
                // Can't have more concurreny than bufferSize, buffer would
                // be full so there's no point.
                val maxRunning = min(concurrency, bufferSize)

                // Maintain set of tasks that are still running, so we can
                // limit concurrency based on the size.
                val runningTasks = mutableSetOf<Deferred<R>>()

                collect { item ->
                    // If we are at max concurrenct wait for any one of
                    // running jobs to finish.
                    if (runningTasks.size >= maxRunning) {
                        // Remove from set once finished.
                        runningTasks -= select<Deferred<R>> {
                            for (task in runningTasks) {
                                task.onJoin { task }
                            }
                        }
                    }

                    // Create next task.
                    val nextTask = async(start = CoroutineStart.LAZY) { block(item) }

                    // Wait for buffer to empty.
                    send(nextTask)

                    // Start next task since we now have room for more
                    // concurrent tasks.
                    nextTask.start()
                    runningTasks += nextTask
                }

                // Might as well.
                runningTasks.clear()
            }

            for (item in buffer) {
                emit(item.await())
            }
        }
    }
}

@API(status = API.Status.EXPERIMENTAL, since = "4.0.0")
fun <T, R> Flow<T>.concurrentMap(
    scope: CoroutineScope,
    concurrencyLevel: Int,
    transform: suspend (T) -> R
): Flow<R> = this
    .map { scope.async { transform(it) } }
    .buffer(concurrencyLevel)
    .map { it.await() }

@API(status = API.Status.EXPERIMENTAL, since = "4.0.0")
inline fun <T, R> Flow<T>.mapInOrder(
    concurrencyLevel: Int,
    crossinline map: suspend (T) -> R
): Flow<R> =
    Semaphore(permits = concurrencyLevel).let { semaphore ->
        channelFlow {
            collect {
                semaphore.acquire()
                send(async { map(it) })
            }
        }
            .map { it.await() }
            .onEach { semaphore.release() }
    }
