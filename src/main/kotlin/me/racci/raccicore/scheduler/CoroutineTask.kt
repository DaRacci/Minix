package me.racci.raccicore.scheduler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import me.racci.raccicore.RacciPlugin
import me.racci.raccicore.utils.now
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CoroutineTask : ITask {

    constructor(
        plugin: RacciPlugin,
        task: suspend Pair<RacciPlugin, CoroutineScope>.() -> Unit
    ) {
        owner = plugin
        this.task = task
    }

    constructor(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable
    ) {
        owner = plugin
        this.runnable = runnable
    }

    override val owner: RacciPlugin
    override var taskID by Delegates.notNull<Int>() ; internal set
    override var async: Boolean = false             ; internal set
    internal var period by Delegates.notNull<Duration>()
    internal var job by Delegates.notNull<Job>()
    internal var task: suspend Pair<RacciPlugin, CoroutineScope>.() -> Unit by Delegates.notNull()
    internal var runnable: CoroutineRunnable? = null
    val createdAt = now().nanosecondsOfSecond

    override val cancelled: Boolean
        get() = period == CANCEL

    override fun cancel() {
        CoroutineScheduler.scope.launch{CoroutineScheduler.cancelTask(taskID)}
    }

    internal suspend fun cancel0(): Boolean {
        period = CANCEL
        if(job.isActive) {
            job.cancelAndJoin()
        } else return false
        return true
    }

    internal fun async(): CoroutineTask {
        this.async = true
        return this
    }

    internal fun sync(): CoroutineTask {
        this.async = false
        return this
    }

    companion object {
        val ERROR                 = Duration.milliseconds(0)
        val NO_REPEATING          = Duration.microseconds(-1L)
        val CANCEL                = Duration.microseconds(-2L)
        val PROCESS_FOR_FUTURE    = Duration.microseconds(-3L)
        val DONE_FOR_FUTURE       = Duration.microseconds(-4L)
    }

}