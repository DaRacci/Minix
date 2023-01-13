package dev.racci.minix.api.extensions

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.WithPlugin
import dev.racci.minix.api.scheduler.CoroutineRunnable
import dev.racci.minix.api.scheduler.CoroutineTask
import kotlin.time.Duration

public typealias SuspendedUnit = suspend (CoroutineRunnable) -> Unit

public inline fun task(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    plugin: MinixPlugin,
    crossinline runnable: SuspendedUnit
): CoroutineTask = task(delayToRun, repeatDelay, false, plugin, runnable)

public inline fun MinixPlugin.task(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    crossinline runnable: SuspendedUnit
): CoroutineTask = task(delayToRun, repeatDelay, this, runnable)

public inline fun WithPlugin<*>.task(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    crossinline runnable: SuspendedUnit
): CoroutineTask = plugin.task(delayToRun, repeatDelay, runnable)

public inline fun taskAsync(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    plugin: MinixPlugin,
    crossinline runnable: SuspendedUnit
): CoroutineTask = task(delayToRun, repeatDelay, true, plugin, runnable)

public inline fun MinixPlugin.taskAsync(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    crossinline runnable: SuspendedUnit
): CoroutineTask = taskAsync(delayToRun, repeatDelay, this, runnable)

public inline fun WithPlugin<*>.taskAsync(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    crossinline runnable: SuspendedUnit
): CoroutineTask = plugin.taskAsync(delayToRun, repeatDelay, runnable)

public inline fun task(
    delayToRun: Duration? = null,
    repeatDelay: Duration? = null,
    async: Boolean,
    plugin: MinixPlugin,
    crossinline runnable: SuspendedUnit
): CoroutineTask = scheduler(runnable).run {
    // run runAsyncTask if async is true and run on timer if delayToRun or repeatDelay is not null
    when {
        repeatDelay != null -> if (async) { runAsyncTaskTimer(plugin, delayToRun ?: Duration.ZERO, repeatDelay) } else { runTaskTimer(plugin, delayToRun ?: Duration.ZERO, repeatDelay) }
        delayToRun != null -> if (async) { runAsyncTaskLater(plugin, delayToRun) } else { runTaskLater(plugin, delayToRun) }
        else -> if (async) { runAsyncTask(plugin) } else { runTask(plugin) }
    }
}

public inline fun scheduler(
    crossinline runnable: SuspendedUnit
): CoroutineRunnable = object : CoroutineRunnable() {
    override suspend fun run() {
        runnable(this)
    }
}
