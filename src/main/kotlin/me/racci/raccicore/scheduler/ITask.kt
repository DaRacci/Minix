package me.racci.raccicore.scheduler

import me.racci.raccicore.RacciPlugin

/**
 * Represents a task being executed
 * by the [CoroutineScheduler]
 */
interface ITask {

    /**
     * The unique ID of the task.
     */
    val taskID: Int

    /**
     * The plugin which starting and owns
     * this task.
     */
    val owner: RacciPlugin

    /**
     * Weather the task is being executed off the
     * main bukkit thread.
     */
    val async: Boolean

    /**
     * True if the task has been cancelled.
     */
    val cancelled: Boolean

    /**
     * Attempts to cancel the task.
     */
    fun cancel()

}