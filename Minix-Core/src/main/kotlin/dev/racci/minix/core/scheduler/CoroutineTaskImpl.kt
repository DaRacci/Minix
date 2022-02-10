package dev.racci.minix.core.scheduler

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.scheduler.CoroutineRunnable
import dev.racci.minix.api.scheduler.CoroutineScheduler
import dev.racci.minix.api.scheduler.CoroutineTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CoroutineTaskImpl : CoroutineTask {

    override val owner: MinixPlugin
    override var name: String by Delegates.notNull()
    override var taskID: Int by Delegates.notNull()
    override var async: Boolean = false; internal set
    override var period: Duration? = null; internal set
    override var job: Job by Delegates.notNull(); internal set
    override var task: suspend Pair<MinixPlugin, CoroutineScope>.() -> Unit by Delegates.notNull()
    override var runnable: CoroutineRunnable? = null; internal set
    override var cancelled: Boolean = false; internal set

    constructor(
        plugin: MinixPlugin,
        task: suspend Pair<MinixPlugin, CoroutineScope>.() -> Unit,
    ) {
        owner = plugin
        this.task = task
    }

    constructor(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
    ) {
        owner = plugin
        this.runnable = runnable
    }

    override suspend fun cancel() {
        CoroutineScheduler.cancelTask(taskID)
    }

    fun cancel0(): Boolean {
        cancelled = true
        if (job.isActive) {
            job.cancel("Plugin shutting down")
        } else return false
        return true
    }

    override fun async(): CoroutineTaskImpl {
        this.async = true
        return this
    }

    override fun sync(): CoroutineTaskImpl {
        this.async = false
        return this
    }
}
