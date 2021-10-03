package me.racci.raccicore.runnables

import org.bukkit.scheduler.BukkitTask

/**
 * Bukkit Task handler.
 */
sealed interface KotlinRunnable : Runnable {
    /**
     * Run the task.
     *
     * @return The created {@link BukkitTask}.
     */
    fun runTask() : BukkitTask

    /**
     * Run the task asynchronously.
     *
     * @return The created {@link BukkitTask}
     */
    fun runTaskAsynchronously() : BukkitTask

    /**
     * Run the task after a specified number of ticks.
     *
     * @param delay The number of ticks to wait.
     * @return The created {@link BukkitTask}
     */
    fun runTaskLater(delay: Long) : BukkitTask

    /**
     * Run the task asynchronously after a specified number of ticks.
     *
     * @param delay The number of ticks to wait.
     * @return The created {@link BukkitTask}
     */
    fun runTaskLaterAsynchronously(delay: Long) : BukkitTask

    /**
     * Run the task repeatedly on a timer.
     *
     * @param delay  The delay before the task is first ran (in ticks).
     * @param period The ticks elapsed before the task is run again.
     * @return The created {@link BukkitTask}
     */
    fun runTaskTimer(delay: Long, period: Long) : BukkitTask

    /**
     * Run the task repeatedly on a timer asynchronously.
     *
     * @param delay  The delay before the task is first ran (in ticks).
     * @param period The ticks elapsed before the task is run again.
     * @return The created {@link BukkitTask}
     */
    fun runTaskTimerAsynchronously(delay: Long, period: Long) : BukkitTask

    /**
     * Cancel the task.
     */
    fun cancel();
}

/**
 * Thread scheduler to handle tasks and asynchronous code.
 */
interface Scheduler {
    /**
     * Run the task after a specified tick delay.
     *
     * @param runnable   The lambda to run.
     * @param ticksLater The amount of ticks to wait before execution.
     * @return The created [BukkitTask].
     */
    fun runLater(
        runnable: Runnable,
        ticksLater: Long
    ): BukkitTask

    /**
     * Run the task repeatedly on a timer.
     *
     * @param runnable The lambda to run.
     * @param delay    The amount of ticks to wait before the first execution.
     * @param repeat   The amount of ticks to wait between executions.
     * @return The created [BukkitTask].
     */
    fun runTimer(
        runnable: Runnable,
        delay: Long,
        repeat: Long
    ): BukkitTask

    /**
     * Run the task repeatedly and asynchronously on a timer.
     *
     * @param runnable The lambda to run.
     * @param delay    The amount of ticks to wait before the first execution.
     * @param repeat   The amount of ticks to wait between executions.
     * @return The created [BukkitTask].
     */
    fun runAsyncTimer(
        runnable: Runnable,
        delay: Long,
        repeat: Long
    ): BukkitTask

    /**
     * Run the task.
     *
     * @param runnable The lambda to run.
     * @return The created [BukkitTask].
     */
    fun run(runnable: Runnable): BukkitTask

    /**
     * Run the task asynchronously.
     *
     * @param runnable The lambda to run.
     * @return The created [BukkitTask].
     */
    fun runAsync(runnable: Runnable): BukkitTask

    /**
     * Schedule the task to be ran repeatedly on a timer.
     *
     * @param runnable The lambda to run.
     * @param delay    The amount of ticks to wait before the first execution.
     * @param repeat   The amount of ticks to wait between executions.
     * @return The id of the task.
     */
    fun syncRepeating(
        runnable: Runnable,
        delay: Long,
        repeat: Long
    ): Int

    /**
     * Cancel all running tasks from the linked [me.racci.raccicore.RacciPlugin].
     */
    fun cancelAll()
}