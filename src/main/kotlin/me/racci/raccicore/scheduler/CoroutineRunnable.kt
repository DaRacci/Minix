package me.racci.raccicore.scheduler

import kotlinx.coroutines.launch
import me.racci.raccicore.RacciPlugin

abstract class CoroutineRunnable : ICoroutineRunnable {
    private var task: ITask? = null

    /**
     * This should only be used within the [run] stage,
     * otherwise this will result in an [IllegalStateException]
     * @throws IllegalStateException
     */
    val plugin get() = task?.owner ?: throw IllegalStateException()

    override fun runTask(
        plugin: RacciPlugin
    ): ITask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runTask(plugin, this))
    }

    override fun runTaskLater(
        plugin: RacciPlugin,
        delay: Long,
    ): ITask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runTaskLater(plugin, this, delay))
    }

    override fun runTaskTimer(
        plugin: RacciPlugin,
        delay: Long,
        period: Long
    ): ITask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runTaskTimer(plugin, this, delay, period))
    }

    override fun runAsyncTask(
        plugin: RacciPlugin
    ): ITask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runAsyncTask(plugin, this))
    }

    override fun runAsyncTaskLater(
        plugin: RacciPlugin,
        delay: Long
    ): ITask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runAsyncTaskLater(plugin, this, delay))
    }

    override fun runAsyncTaskTimer(
        plugin: RacciPlugin,
        delay: Long,
        period: Long
    ): ITask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runAsyncTaskTimer(plugin, this, delay, period))
    }


    override val taskID: Int
        get() {
            checkScheduled()
            return task!!.taskID
        }

    override val cancelled: Boolean
        get() {
            checkScheduled()
            return task!!.cancelled
        }

    override fun cancel() {
        CoroutineScheduler.scope.launch{CoroutineScheduler.cancelTask(taskID)}
    }

    private fun checkScheduled() {
        checkNotNull(task) {"Not scheduled yet"}
    }

    private fun checkNotYetScheduled() {
        check(task == null) {"Already scheduled as " + task!!.taskID}
    }

    private fun setupTask(task: ITask): ITask {
        this.task = task
        return task
    }

}