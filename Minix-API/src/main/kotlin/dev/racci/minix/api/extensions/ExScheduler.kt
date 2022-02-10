@file:Suppress("UNUSED", "UNCHECKED_CAST")

package dev.racci.minix.api.extensions

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.scheduler.CoroutineRunnable
import kotlin.time.Duration

typealias SuspendedUnit = suspend (CoroutineRunnable) -> Unit

inline fun task(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    plugin: MinixPlugin,
    crossinline runnable: SuspendedUnit
) = task(delayToRun, repeatDelay, false, plugin, runnable)

inline fun MinixPlugin.task(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    crossinline runnable: SuspendedUnit
) = task(delayToRun, repeatDelay, this, runnable)

inline fun WithPlugin<*>.task(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    crossinline runnable: SuspendedUnit
) = plugin.task(delayToRun, repeatDelay, runnable)

inline fun taskAsync(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    plugin: MinixPlugin,
    crossinline runnable: SuspendedUnit
) = task(delayToRun, repeatDelay, true, plugin, runnable)

inline fun MinixPlugin.taskAsync(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    crossinline runnable: SuspendedUnit
) = taskAsync(delayToRun, repeatDelay, this, runnable)

inline fun WithPlugin<*>.taskAsync(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    crossinline runnable: SuspendedUnit
) = plugin.taskAsync(delayToRun, repeatDelay, runnable)

inline fun task(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    async: Boolean,
    plugin: MinixPlugin,
    crossinline runnable: SuspendedUnit
) = scheduler(runnable).run {
    // run runAsyncTask if async is true and run on timer if delayToRun or repeatDelay is not null
    if (repeatDelay != null) {
        if (async) {
            runAsyncTaskTimer(plugin, delayToRun ?: Duration.ZERO, repeatDelay)
        } else {
            runTaskTimer(
                plugin,
                delayToRun ?: Duration.ZERO,
                repeatDelay
            )
        }
    } else if (delayToRun != null) {
        if (async) {
            runAsyncTaskLater(plugin, delayToRun)
        } else runTaskLater(plugin, delayToRun)
    } else if (async) {
        runAsyncTask(plugin)
    } else runTask(plugin)
}

inline fun scheduler(
    crossinline runnable: SuspendedUnit
) = object : CoroutineRunnable() {
    override suspend fun run() {
        runnable(this)
    }
}
