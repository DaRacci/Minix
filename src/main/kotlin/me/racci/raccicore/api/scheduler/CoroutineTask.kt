package me.racci.raccicore.api.scheduler

import me.racci.raccicore.api.plugin.RacciPlugin

/**
 * Represents a task being executed
 * by the [CoroutineScheduler]
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

//    val period: Duration
//
//    val job: Job?
//
//    val task: suspend Pair<RacciPlugin, CoroutineScope>.() -> Unit
//
//    val runnable: CoroutineRunnable?
//
//    val createdAt: Instant

    fun async(): CoroutineTask

    fun sync(): CoroutineTask

    /**
     * Attempts to cancel the task.
     */
    fun cancel()

}