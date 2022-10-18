package dev.racci.minix.api.scheduler

import dev.racci.minix.api.plugin.MinixPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.Duration

// TODO add extensions so i can not have to add plugin into each thingy
abstract class CoroutineRunnable {

    private var task: CoroutineTask? = null
    private var taskName: String? = null

    /**
     * This should only be used within the [run] stage,
     * otherwise this will result in an [IllegalStateException].
     * @throws IllegalStateException
     */
    val plugin get() = task?.owner ?: error { "Don't call the plugin val if this task doesn't have an owner." }

    val taskID: Int
        get() {
            checkScheduled()
            return task!!.taskID
        }

    val name: String
        get() {
            checkScheduled()
            return task!!.name
        }

    val cancelled: Boolean
        get() {
            checkScheduled()
            return task!!.keepRunning.value
        }

    abstract suspend fun run()

    fun named(name: String): CoroutineRunnable {
        taskName = name
        return this
    }

    fun runTask(
        plugin: MinixPlugin
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runTask(plugin, this))
    }

    fun runTaskLater(
        plugin: MinixPlugin,
        delay: Duration
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runTaskLater(plugin, this, delay))
    }

    fun runTaskTimer(
        plugin: MinixPlugin,
        delay: Duration,
        period: Duration
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runTaskTimer(plugin, this, delay, period))
    }

    fun runAsyncTask(
        plugin: MinixPlugin
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runAsyncTask(plugin, runnable = this))
    }

    fun runAsyncTaskLater(
        plugin: MinixPlugin,
        delay: Duration
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runAsyncTaskLater(plugin, this, delay))
    }

    fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        delay: Duration,
        period: Duration
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runAsyncTaskTimer(plugin, this, delay, period))
    }

    fun cancel() {
        CoroutineScope(CoroutineScheduler.parentJob).launch { CoroutineScheduler.cancelTask(taskID) }
    }

    private fun checkScheduled() {
        checkNotNull(task) { "Not scheduled yet" }
    }

    private fun checkNotYetScheduled() {
        check(task == null) { "Already scheduled as " + task!!.taskID }
    }

    private fun setupTask(task: CoroutineTask): CoroutineTask {
        this.task = task
        this.task!!.name = taskName ?: taskID.toString()
        return task
    }
}
