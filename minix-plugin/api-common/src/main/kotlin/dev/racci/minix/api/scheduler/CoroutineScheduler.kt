package dev.racci.minix.api.scheduler

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.getKoin
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

public typealias CoroutineBlock = suspend (MinixPlugin, CoroutineScope) -> Unit

public interface CoroutineScheduler {

    public val parentJob: CompletableJob

    /**
     * @return an [IntArray] which represents this plugins active jobs.
     */
    public fun activateTasks(plugin: MinixPlugin): IntArray?

    /**
     * Initiates an orderly shutdown, if the task timer is currently running,
     * it will be allowed to finish, but then not run again.
     * Invoking this will have no effect if already shut down.
     *
     * @return true if the task was successfully shut down.
     */
    public suspend fun shutdownTask(taskID: Int): Boolean

    /**
     * Immediately stops the task event if there is a running job.
     *
     * @return true if the task was successfully stopped.
     */
    public fun cancelTask(taskID: Int): Boolean

    /**
     * Checks if the task is currently active.
     * A task that has finished and does not repeat,
     * will not be active ever again.
     *
     * @return true if the task is currently active.
     */
    public fun isCurrentlyRunning(taskID: Int): Boolean

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @return An [CoroutineTask] that contains the id number
     */
    public fun runTask(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runTask(
        plugin: MinixPlugin,
        name: String? = null,
        task: CoroutineBlock
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runTask(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @param delay The delay before this task starts.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runTaskLater(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Duration
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @param delay The delay before this task starts.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runTaskLater(
        plugin: MinixPlugin,
        name: String? = null,
        task: CoroutineBlock,
        delay: Duration
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @param delay The delay before this task starts.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runTaskLater(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Duration
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until keepRunning.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @param delay The delay before this task starts.
     * @param period The delay between each run on this task.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runTaskTimer(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Duration,
        period: Duration
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until keepRunning.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @param delay The delay before this task starts.
     * @param period The delay between each run on this task.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runTaskTimer(
        plugin: MinixPlugin,
        name: String? = null,
        task: CoroutineBlock,
        delay: Duration,
        period: Duration
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until keepRunning.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @param delay The delay before this task starts.
     * @param period The delay between each run on this task.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runTaskTimer(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Duration,
        period: Duration
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @return An [CoroutineTask] that contains the id number
     */
    public fun runAsyncTask(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runAsyncTask(
        plugin: MinixPlugin,
        name: String? = null,
        task: CoroutineBlock
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runAsyncTask(
        plugin: MinixPlugin,
        name: String? = null,
        runnable: CoroutineRunnable
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @param delay The delay before this task starts.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runAsyncTaskLater(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Duration
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @param delay The delay before this task starts.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runAsyncTaskLater(
        plugin: MinixPlugin,
        name: String? = null,
        task: CoroutineBlock,
        delay: Duration
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @param delay The delay before this task starts.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runAsyncTaskLater(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Duration
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until keepRunning.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @param delay The delay before this task starts.
     * @param period The delay between each run on this task.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Duration,
        period: Duration
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until keepRunning.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @param delay The delay before this task starts.
     * @param period The delay between each run on this task.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        name: String? = null,
        task: CoroutineBlock,
        delay: Duration,
        period: Duration
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until keepRunning.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @param delay The delay before this task starts.
     * @param period The delay between each run on this task.
     * @return An [CoroutineTask] that contains the id number.
     */
    public fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Duration,
        period: Duration
    ): CoroutineTask

    public companion object : CoroutineScheduler by getKoin().get()
}
