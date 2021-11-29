package me.racci.raccicore.api.scheduler

import kotlinx.coroutines.CoroutineScope
import me.racci.raccicore.api.plugin.RacciPlugin

interface CoroutineScheduler {

    /**
     * Attempts to remove a task from the scheduler.
     *
     * @param taskID The task to remove and cancel.
     * @return If the task was successfully cancelled and removed.
     */
    suspend fun cancelTask(taskID: Int): Boolean

    /**
     * Attempts to remove all active tasks of
     * the [RacciPlugin] from the scheduler
     *
     * @param plugin The plugin
     */
    suspend fun cancelAllTasks(plugin: RacciPlugin)

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
     * @param plugin The [RacciPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @return An [CoroutineTask] that contains the id number
     */
    fun runTask(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runTask(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runTask(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runTaskLater(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask,
        delay: Long
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runTaskLater(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit, delay: Long
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runTaskLater(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable,
        delay: Long
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until cancelled.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runTaskTimer(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask,
        delay: Long, period: Long
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until cancelled.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runTaskTimer(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit,
        delay: Long,
        period: Long
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * on the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until cancelled.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runTaskTimer(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
        period: Long
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @return An [CoroutineTask] that contains the id number
     */
    fun runAsyncTask(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runAsyncTask(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runAsyncTask(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runAsyncTaskLater(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask,
        delay: Long
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runAsyncTaskLater(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit,
        delay: Long
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runAsyncTaskLater(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable,
        delay: Long
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until cancelled.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param coroutineTask The task to run.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runAsyncTaskTimer(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask,
        delay: Long,
        period: Long
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until cancelled.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param task The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runAsyncTaskTimer(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit,
        delay: Long,
        period: Long
    ): CoroutineTask

    /**
     * Returns an [CoroutineTask] that will run once
     * off the main bukkit thread after the specified
     * number of ticks in [delay] is reached and will
     * repeat every [period] ticks until cancelled.
     *
     * @param plugin The [RacciPlugin] who owns the task.
     * @param runnable The [Unit] to create a task from.
     * @return An [CoroutineTask] that contains the id number.
     */
    fun runAsyncTaskTimer(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
        period: Long
    ): CoroutineTask

}