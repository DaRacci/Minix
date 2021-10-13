package me.racci.raccicore.utils

import me.racci.raccicore.utils.extensions.WithPlugin
import me.racci.raccicore.utils.extensions.task
import org.bukkit.plugin.Plugin
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

internal val coroutineContextTakes = ConcurrentHashMap<CoroutineContext, TakeValues>()
internal data class TakeValues(val startTimeMilliseconds: Long, val takeTimeMillisecond: Long) {
    fun wasTimeExceeded() = System.currentTimeMillis() - startTimeMilliseconds - takeTimeMillisecond >= 0
}

@OptIn(ExperimentalTime::class)
suspend fun WithPlugin<*>.takeMaxPerTick(
        time: Duration
) = plugin.takeMaxPerTick(time)

@OptIn(ExperimentalTime::class)
suspend fun Plugin.takeMaxPerTick(time: Duration) {
    val takeValues = getTakeValuesOrNull(coroutineContext)

    if(takeValues == null) {
        // registering take max at current millisecond
        registerCoroutineContextTakes(coroutineContext, time)
    } else {
        // checking if this exceeded the max time of execution
        if(takeValues.wasTimeExceeded()) {
            unregisterCoroutineContextTakes(coroutineContext)
            suspendCoroutine<Unit> { continuation ->
                task(1) {
                    continuation.resume(Unit)
                }
            }
        }
    }
}

internal fun getTakeValuesOrNull(
        coroutineContext: CoroutineContext
): TakeValues? = coroutineContextTakes[coroutineContext]

@OptIn(ExperimentalTime::class)
internal fun registerCoroutineContextTakes(
        coroutineContext: CoroutineContext,
        time: Duration
) {
    coroutineContextTakes[coroutineContext] = TakeValues(System.currentTimeMillis(), time.inWholeMilliseconds)
}

internal fun unregisterCoroutineContextTakes(
        coroutineContext: CoroutineContext
) {
    coroutineContextTakes.remove(coroutineContext)
}