package dev.racci.minix.api.scheduler

import dev.racci.minix.api.plugin.MinixPlugin
import kotlinx.atomicfu.AtomicBoolean
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
    val taskID: Int

    /**
     * The Name of the task.
     */
    var name: String

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
     * If this task will continue running
     */
    val keepRunning: AtomicBoolean

    /**
     * The period of time between each execution
     */
    val period: Duration?

    val job: Job?
    val task: suspend (MinixPlugin, CoroutineScope) -> Unit
    val runnable: CoroutineRunnable?

    /**
     * Initiates an orderly shutdown, if the task timer is currently running,
     * it will be allowed to finish, but then not run again.
     * Invoking this will have no effect if already shut down.
     *
     * @return true if the task was successfully shut down.
     */
    fun shutdown(): Boolean

    /**
     * Immediately stops the task event if there is a running job.
     *
     * @return Returns true if the task was successfully stopped.
     */
    fun cancel(): Boolean

    fun async(): CoroutineTask

    fun sync(): CoroutineTask
}
