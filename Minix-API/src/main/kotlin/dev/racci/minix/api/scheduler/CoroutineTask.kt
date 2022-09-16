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

    /** The unique ID of the task. */
    val taskID: Int

    /** The name of the task. */
    var name: String

    /** The plugin, which started and owns this task. */
    val owner: MinixPlugin

    /** If the task is being executed off the main bukkit thread. */
    val async: Boolean

    /** If this task is able to repeat. */
    val keepRunning: AtomicBoolean

    /** The delay between each execution. */
    val period: Duration?

    val job: Job?
    val task: suspend (MinixPlugin, CoroutineScope) -> Unit
    val runnable: CoroutineRunnable?

    /**
     * Initiates an orderly shutdown.
     * If the task timer is currently running, it will be allowed to finish, but then not run again.
     * Invoking this will have no effect if already shutdown.
     *
     * @return if the task was successfully shutdown.
     */
    fun shutdown(): Boolean

    /**
     * Immediately stops the task event if there is a running job.
     *
     * @return If the task was successfully stopped.
     */
    fun cancel(): Boolean

    /** Marks that the task should execute asynchronously. */
    fun async(): CoroutineTask

    /** Marks that the task should execute synchronously. */
    fun sync(): CoroutineTask
}
