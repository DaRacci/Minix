package dev.racci.minix.core.scheduler

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.scheduler.CoroutineRunnable
import dev.racci.minix.api.scheduler.CoroutineScheduler
import dev.racci.minix.api.scheduler.CoroutineTask
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.properties.Delegates
import kotlin.time.Duration

public class CoroutineTaskImpl(
    override val owner: MinixPlugin
) : CoroutineTask {

    override var name: String by Delegates.notNull()
    override var taskID: Int by Delegates.notNull()
    override var async: Boolean = false
    override var period: Duration? = null
    override var job: Job by Delegates.notNull()
    override var keepRunning: AtomicBoolean = atomic(true)

    override var task: suspend (MinixPlugin, CoroutineScope) -> Unit by Delegates.notNull()
    override var runnable: CoroutineRunnable? = null

    public constructor(
        plugin: MinixPlugin,
        task: suspend (MinixPlugin, CoroutineScope) -> Unit
    ) : this(plugin) {
        this.task = task
    }

    public constructor(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable
    ) : this(plugin) {
        this.runnable = runnable
    }

    override fun cancel(): Boolean =
        CoroutineScheduler.cancelTask(taskID)

    internal fun cancel0(): Boolean {
        keepRunning.lazySet(false)
        return if (job.isActive) {
            job.cancel()
            true
        } else false
    }

    override fun async(): CoroutineTaskImpl {
        this.async = true
        return this
    }

    override fun sync(): CoroutineTaskImpl {
        this.async = false
        return this
    }

    override fun shutdown(): Boolean {
        return keepRunning.getAndSet(false)
    }
}
