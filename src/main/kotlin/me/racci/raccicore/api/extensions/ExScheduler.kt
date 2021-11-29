@file:Suppress("UNUSED", "UNCHECKED_CAST")
package me.racci.raccicore.api.extensions

import me.racci.raccicore.api.plugin.RacciPlugin
import me.racci.raccicore.api.scheduler.CoroutineRunnable

inline fun task(
    delayToRun: Long = 0,
    repeatDelay: Long = -1,
    plugin: RacciPlugin,
    crossinline runnable: CoroutineRunnable.() -> Unit
) = task(delayToRun, repeatDelay, false, plugin, runnable)

inline fun RacciPlugin.task(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        crossinline runnable: CoroutineRunnable.() -> Unit
) = task(delayToRun, repeatDelay, this, runnable)

inline fun WithPlugin<*>.task(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        crossinline runnable: CoroutineRunnable.() -> Unit
) = plugin.task(delayToRun, repeatDelay, runnable)

inline fun taskAsync(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        plugin: RacciPlugin,
        crossinline runnable: CoroutineRunnable.() -> Unit
) = task(delayToRun, repeatDelay, true, plugin, runnable)

inline fun RacciPlugin.taskAsync(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        crossinline runnable: CoroutineRunnable.() -> Unit
) = taskAsync(delayToRun, repeatDelay, this, runnable)

inline fun WithPlugin<*>.taskAsync(
        delayToRun: Long = 0,
        repeatDelay: Long = -1,
        crossinline runnable: CoroutineRunnable.() -> Unit
) = plugin.taskAsync(delayToRun, repeatDelay, runnable)

inline fun task(
        delayToRun: Long,
        repeatDelay: Long = -1,
        async: Boolean,
        plugin: RacciPlugin,
        crossinline runnable: CoroutineRunnable.() -> Unit
) = scheduler(runnable).run {
    if (repeatDelay > -1) if (async) runAsyncTaskTimer(plugin, delayToRun, repeatDelay) else runTaskTimer(plugin, delayToRun, repeatDelay)
    else if (delayToRun > 0) if (async) runAsyncTaskLater(plugin, delayToRun) else runTaskLater(plugin, delayToRun)
    else if (async) runAsyncTask(plugin) else runTask(plugin)
}

inline fun scheduler(
    crossinline runnable: CoroutineRunnable.() -> Unit
) = object : CoroutineRunnable() {
    override suspend fun run() {
        this.runnable()
    }
}