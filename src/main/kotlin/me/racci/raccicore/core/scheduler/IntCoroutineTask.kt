package me.racci.raccicore.core.scheduler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import me.racci.raccicore.api.plugin.RacciPlugin
import me.racci.raccicore.api.scheduler.CoroutineRunnable
import me.racci.raccicore.api.scheduler.CoroutineTask
import org.jetbrains.annotations.ApiStatus
import kotlin.properties.Delegates
import kotlin.time.Duration
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.ExperimentalTime

@ApiStatus.Internal
@OptIn(ExperimentalTime::class)
class IntCoroutineTask: CoroutineTask {

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
    override var taskID by Delegates.notNull<Int>()
    override var async: Boolean = false
    var period by Delegates.notNull<Duration>()
    var job by Delegates.notNull<Job>()
    var task: suspend Pair<RacciPlugin, CoroutineScope>.() -> Unit by Delegates.notNull()
    var runnable: CoroutineRunnable? = null
    //val createdAt = now()

    override val cancelled: Boolean
        get() = period == CANCEL

    override fun cancel() {
        CoroutineScheduler.scope.launch{CoroutineScheduler.cancelTask(taskID)}
    }

    suspend fun cancel0(): Boolean {
        period = CANCEL
        if(job.isActive) {
            job.cancelAndJoin()
        } else return false
        return true
    }

    override fun async(): IntCoroutineTask {
        this.async = true
        return this
    }

    override fun sync(): IntCoroutineTask {
        this.async = false
        return this
    }

    companion object {
        val NO_REPEATING = (-1L).microseconds
        val CANCEL       = (-2L).microseconds
    }

}