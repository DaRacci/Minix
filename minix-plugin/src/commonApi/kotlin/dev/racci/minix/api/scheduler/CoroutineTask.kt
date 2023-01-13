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
public interface CoroutineTask {

    /** The unique ID of the task. */
    public val taskID: Int

    /** The name of the task. */
    public var name: String

    /** The plugin, which started and owns this task. */
    public val owner: MinixPlugin

    /** If the task is being executed off the main bukkit thread. */
    public val async: Boolean

    /** If this task is able to repeat. */
    public val keepRunning: AtomicBoolean

    /** The delay between each execution. */
    public val period: Duration?

    public val job: Job?
    public val task: suspend (MinixPlugin, CoroutineScope) -> Unit
    public val runnable: CoroutineRunnable?

    /**
     * Initiates an orderly shutdown.
     * If the task timer is currently running, it will be allowed to finish, but then not run again.
     * Invoking this will have no effect if already shutdown.
     *
     * @return if the task was successfully shutdown.
     */
    public fun shutdown(): Boolean

    /**
     * Immediately stops the task event if there is a running job.
     *
     * @return If the task was successfully stopped.
     */
    public fun cancel(): Boolean

    /** Marks that the task should execute asynchronously. */
    public fun async(): CoroutineTask

    /** Marks that the task should execute synchronously. */
    public fun sync(): CoroutineTask
}
