package dev.racci.minix.core.scheduler

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.scheduler.CoroutineRunnable
import dev.racci.minix.api.scheduler.CoroutineTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CoroutineTaskImpl : CoroutineTask {

    override val owner: MinixPlugin
    override var taskID by Delegates.notNull<Int>()
    override var async: Boolean = false
    override var period by Delegates.notNull<Duration>()
    override var job by Delegates.notNull<Job>()
    override var task: suspend Pair<MinixPlugin, CoroutineScope>.() -> Unit by Delegates.notNull()
    override var runnable: CoroutineRunnable? = null

    override val cancelled: Boolean
        get() = period == CANCEL

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

    override fun cancel() {
        CoroutineSchedulerImpl.scope.launch { CoroutineSchedulerImpl.cancelTask(taskID) }
    }

    suspend fun cancel0(): Boolean {
        period = CANCEL
        if (job.isActive) {
            job.cancelAndJoin()
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

    companion object {

        val NO_REPEATING = (-1L).microseconds
        val CANCEL = (-2L).microseconds
    }
}
