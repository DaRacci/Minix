package dev.racci.minix.api.scheduler

import dev.racci.minix.api.plugin.MinixPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.Duration

// TODO -> Rewrite without any use of the bukkit scheduler
// TODO add extensions so i can not have to add plugin into each thingy
public abstract class CoroutineRunnable {

    private var task: CoroutineTask? = null
    private var taskName: String? = null

    /**
     * This should only be used within the [run] stage,
     * otherwise this will result in an [IllegalStateException].
     * @throws IllegalStateException
     */
    public val plugin: MinixPlugin get() = task?.owner ?: error { "Don't call the plugin val if this task doesn't have an owner." }

    public val taskID: Int
        get() {
            checkScheduled()
            return task!!.taskID
        }

    public val name: String
        get() {
            checkScheduled()
            return task!!.name
        }

    public val cancelled: Boolean
        get() {
            checkScheduled()
            return task!!.keepRunning.value
        }

    public abstract suspend fun run()

    public fun named(name: String): CoroutineRunnable {
        taskName = name
        return this
    }

    public fun runTask(
        plugin: MinixPlugin
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runTask(plugin, this))
    }

    public fun runTaskLater(
        plugin: MinixPlugin,
        delay: Duration
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runTaskLater(plugin, this, delay))
    }

    public fun runTaskTimer(
        plugin: MinixPlugin,
        delay: Duration,
        period: Duration
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runTaskTimer(plugin, this, delay, period))
    }

    public fun runAsyncTask(
        plugin: MinixPlugin
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runAsyncTask(plugin, runnable = this))
    }

    public fun runAsyncTaskLater(
        plugin: MinixPlugin,
        delay: Duration
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runAsyncTaskLater(plugin, this, delay))
    }

    public fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        delay: Duration,
        period: Duration
    ): CoroutineTask {
        checkNotYetScheduled()
        return setupTask(CoroutineScheduler.runAsyncTaskTimer(plugin, this, delay, period))
    }

    public fun cancel() {
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
