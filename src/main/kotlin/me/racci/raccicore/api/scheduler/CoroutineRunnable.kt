@file:Suppress("UNUSED", "UNCHECKED_CAST", "SameParameterValue", "MemberVisibilityCanBePrivate")
package me.racci.raccicore.api.scheduler

import kotlinx.coroutines.launch
import me.racci.raccicore.api.plugin.RacciPlugin
import me.racci.raccicore.core.scheduler.CoroutineScheduler

abstract class CoroutineRunnable {
    private var task: CoroutineTask? = null

    /**
     * This should only be used within the [run] stage,
     * otherwise this will result in an [IllegalStateException]
     * @throws IllegalStateException
     */
    val plugin get() = task?.owner ?: throw IllegalStateException()

    abstract suspend fun run()

    fun runTask(
        plugin: RacciPlugin
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runTask(plugin, this))
    }

    fun runTaskLater(
        plugin: RacciPlugin,
        delay: Long,
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runTaskLater(plugin, this, delay))
    }

    fun runTaskTimer(
        plugin: RacciPlugin,
        delay: Long,
        period: Long
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runTaskTimer(plugin, this, delay, period))
    }

    fun runAsyncTask(
        plugin: RacciPlugin
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runAsyncTask(plugin, this))
    }

    fun runAsyncTaskLater(
        plugin: RacciPlugin,
        delay: Long
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runAsyncTaskLater(plugin, this, delay))
    }

    fun runAsyncTaskTimer(
        plugin: RacciPlugin,
        delay: Long,
        period: Long
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runAsyncTaskTimer(plugin, this, delay, period))
    }

    val taskID: Int
        get() {
            checkScheduled()
            return task!!.taskID
        }

    val cancelled: Boolean
        get() {
            checkScheduled()
            return task!!.cancelled
        }

    fun cancel() {
        CoroutineScheduler.scope.launch{CoroutineScheduler.cancelTask(taskID)}
    }

    private fun checkScheduled() {
        checkNotNull(task) {"Not scheduled yet"}
    }

    private fun checkNotYetScheduled() {
        check(task == null) {"Already scheduled as " + task!!.taskID}
    }

    private fun setupTask(task: CoroutineTask): CoroutineTask {
        this.task = task
        return task
    }

}