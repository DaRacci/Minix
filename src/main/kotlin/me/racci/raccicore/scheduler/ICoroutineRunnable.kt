package me.racci.raccicore.scheduler

import me.racci.raccicore.RacciPlugin

interface ICoroutineRunnable {

    val taskID: Int

    val cancelled: Boolean

    suspend fun run()

    fun cancel()

    fun runTask(plugin: RacciPlugin): ITask

    fun runTaskLater(plugin: RacciPlugin, delay: Long): ITask

    fun runTaskTimer(plugin: RacciPlugin, delay: Long, period: Long): ITask

    fun runAsyncTask(plugin: RacciPlugin): ITask

    fun runAsyncTaskLater(plugin: RacciPlugin, delay: Long): ITask

    fun runAsyncTaskTimer(plugin: RacciPlugin, delay: Long, period: Long): ITask

}