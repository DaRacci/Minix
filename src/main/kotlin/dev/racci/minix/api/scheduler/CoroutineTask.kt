package dev.racci.minix.api.scheduler

import dev.racci.minix.api.plugin.MinixPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.time.Duration

/**
 * Represents a task being executed
 * by the [CoroutineScheduler].
 */
interface CoroutineTask {

    /**
     * The unique ID of the task.
     */
    var taskID: Int

    /**
     * The plugin which starting and owns
     * this task.
     */
    val owner: MinixPlugin

    /**
     * Weather the task is being executed off the
     * main bukkit thread.
     */
    val async: Boolean

    /**
     * True if the task has been cancelled.
     */
    val cancelled: Boolean

    val period: Duration

    val job: Job?

    val task: suspend Pair<MinixPlugin, CoroutineScope>.() -> Unit

    val runnable: CoroutineRunnable?

    fun async(): CoroutineTask

    fun sync(): CoroutineTask

    /**
     * Attempts to cancel the task.
     */
    fun cancel()
}
