@file:Suppress("Unused")

package dev.racci.minix.api.scheduler

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.getKoin
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

@Suppress("ComplexInterface")
interface CoroutineScheduler {

    val scope: CoroutineScope

    /**
     * @returns an IntArray of this plugins currently running tasks.
     * If there are no active tasks it will return null.
     */
    suspend fun activateTasks(plugin: MinixPlugin): IntArray?

    /**
     * Attempts to remove a task from the scheduler.
     *
     * @param taskID The task to remove and cancel.
     * @return If the task was successfully cancelled and removed.
     */
    suspend fun cancelTask(taskID: Int): Boolean

    /**
     * Attempts to find and cancel a task matching this name if it exists.
     */
    suspend fun cancelTask(name: String): Boolean

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
     * Check if the task is currently active.
     * A task that has finished and does not repeat,
     * will not be active ever again.
     *
     * @param name The task to check
     * @return If the task is currently active.
     */
    fun isCurrentlyRunning(name: String): Boolean

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
        name: String? = null,
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
        delay: Duration,
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
        name: String? = null,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
        delay: Duration,
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
        delay: Duration,
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
        delay: Duration,
        period: Duration,
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
        name: String? = null,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
        delay: Duration,
        period: Duration,
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
        delay: Duration,
        period: Duration,
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
        name: String? = null,
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
        name: String? = null,
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
        delay: Duration,
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
        name: String? = null,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
        delay: Duration,
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
        delay: Duration,
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
        delay: Duration,
        period: Duration,
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
        name: String? = null,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
        delay: Duration,
        period: Duration,
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
        delay: Duration,
        period: Duration,
    ): CoroutineTask

    companion object : CoroutineScheduler by getKoin().get()
}
