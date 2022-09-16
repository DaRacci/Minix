package dev.racci.minix.api.utils

import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.extensions.task
import dev.racci.minix.api.plugin.MinixPlugin
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal val coroutineContextTakes = ConcurrentHashMap<CoroutineContext, TakeValues>()

internal data class TakeValues(
    val startTimeMilliseconds: Long,
    val takeTimeMillisecond: Long
) {

    fun wasTimeExceeded() = System.currentTimeMillis() - startTimeMilliseconds - takeTimeMillisecond >= 0
}

suspend fun WithPlugin<*>.takeMaxPerTick(
    time: Duration
) = plugin.takeMaxPerTick(time)

suspend fun MinixPlugin.takeMaxPerTick(time: Duration) {
    val takeValues = getTakeValuesOrNull(coroutineContext)

    if (takeValues == null) {
        // registering take max at current millisecond
        registerCoroutineContextTakes(coroutineContext, time)
    } else {
        // checking if this exceeded the max time of execution
        if (takeValues.wasTimeExceeded()) {
            unregisterCoroutineContext(coroutineContext)
            suspendCoroutine<Unit> { continuation ->
                task(1.milliseconds) {
                    continuation.resume(Unit)
                }
            }
        }
    }
}

internal fun getTakeValuesOrNull(
    coroutineContext: CoroutineContext
): TakeValues? = coroutineContextTakes[coroutineContext]

internal fun registerCoroutineContextTakes(
    coroutineContext: CoroutineContext,
    time: Duration
) {
    coroutineContextTakes[coroutineContext] = TakeValues(System.currentTimeMillis(), time.inWholeMilliseconds)
}

internal fun unregisterCoroutineContext(
    coroutineContext: CoroutineContext
) {
    coroutineContextTakes.remove(coroutineContext)
}
