@file:Suppress("Unused")

package dev.racci.minix.api.scheduler

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.getKoin
import kotlinx.coroutines.CoroutineScope

@Suppress("ComplexInterface")
interface CoroutineScheduler {

    val scope: CoroutineScope

    /**
     * Attempts to remove a task from the scheduler.
     *
     * @param taskID The task to remove and cancel.
     * @return If the task was successfully cancelled and removed.
     */
    suspend fun cancelTask(taskID: Int): Boolean

    /**
     * Attempts to remove all active tasks of
     * the [MinixPlugin] from the scheduler.
     *
     * @param plugin The plugin
     */
    suspend fun cancelAllTasks(plugin: MinixPlugin)

    /**
     * Check if the task is currently active.
     * A task that has finished and does not repeat,
     * will not be active ever again.
     *
     * @param taskID The task to check
     * @return If the task is currently active.
     */
    fun isCurrentlyRunning(taskID: Int): Boolean

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @return An [CoroutineTask] that contains the id number
     */
    fun runTask(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runTask(
        plugin: MinixPlugin,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runTask(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
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
    fun runTaskLater(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Long,
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
    fun runTaskLater(
        plugin: MinixPlugin,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
        delay: Long,
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
    fun runTaskLater(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until cancelled.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @param delay The delay before this task starts.
     * @param period The delay between each run on this task.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runTaskTimer(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Long,
        period: Long,
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until cancelled.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @param delay The delay before this task starts.
     * @param period The delay between each run on this task.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runTaskTimer(
        plugin: MinixPlugin,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
        delay: Long,
        period: Long,
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until cancelled.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @param delay The delay before this task starts.
     * @param period The delay between each run on this task.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runTaskTimer(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
        period: Long,
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @return An [CoroutineTask] that contains the id number
     */
    fun runAsyncTask(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runAsyncTask(
        plugin: MinixPlugin,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runAsyncTask(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
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
    fun runAsyncTaskLater(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Long,
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
    fun runAsyncTaskLater(
        plugin: MinixPlugin,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
        delay: Long,
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
    fun runAsyncTaskLater(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until cancelled.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @param delay The delay before this task starts.
     * @param period The delay between each run on this task.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Long,
        period: Long,
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until cancelled.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @param delay The delay before this task starts.
     * @param period The delay between each run on this task.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
        delay: Long,
        period: Long,
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until cancelled.
     *
     * @param plugin The [MinixPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @param delay The delay before this task starts.
     * @param period The delay between each run on this task.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
        period: Long,
    ): CoroutineTask

    companion object : CoroutineScheduler by getKoin().get()
}
