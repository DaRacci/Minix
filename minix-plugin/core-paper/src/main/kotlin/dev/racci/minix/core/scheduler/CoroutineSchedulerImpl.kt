package dev.racci.minix.core.scheduler

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.collections.clear
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.scheduler.CoroutineBlock
import dev.racci.minix.api.scheduler.CoroutineRunnable
import dev.racci.minix.api.scheduler.CoroutineScheduler
import dev.racci.minix.api.scheduler.CoroutineTask
import dev.racci.minix.core.plugin.Minix
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import org.koin.core.component.get
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration

@Scope(Minix::class)
@Scoped([CoroutineScheduler::class])
@MappedExtension(bindToKClass = CoroutineScheduler::class, name = "Coroutine Scheduler", threadCount = 4)
public class CoroutineSchedulerImpl(override val plugin: Minix) : Extension<Minix>(), CoroutineScheduler {

    override val parentJob: CompletableJob by lazy { SupervisorJob() }

    private val bukkitContext by lazy { get<Minix>().minecraftContext }
    private val ids by lazy { atomic(-1) }
    private val tasks by lazy { ConcurrentHashMap<Int, CoroutineTaskImpl>() }

    public override suspend fun handleLoad() {
        ids.getAndSet(-1)
    }

    public override suspend fun handleUnload() {
        tasks.clear { _, task -> task.cancel() }
        parentJob.complete()
    }

    @OptIn(InternalCoroutinesApi::class)
    private fun handleStart(
        task: CoroutineTaskImpl,
        delay: Duration? = null,
        period: Duration? = null
    ): CoroutineTask {
        task.taskID = ids.incrementAndGet()
        task.period = period

        val context = if (task.async) dispatcher.get() else bukkitContext
        val job = CoroutineScope(parentJob).launch(context, CoroutineStart.LAZY) {
            delay?.let { delay(it) }

            if (period != null) {
                while (task.keepRunning.value) {
                    task.runnable?.run() ?: task.task(task.owner, this)
                    delay(period)
                }
            } else {
                task.period = null
                task.runnable?.run() ?: task.task(task.owner, this)
            }
        }
        task.job = job
        tasks[task.taskID] = task
        job.invokeOnCompletion(
            onCancelling = true,
            invokeImmediately = true
        ) { throwable ->
            tasks[task.taskID]?.let {
                if (throwable != null) {
                    logger.error(throwable) { "Task ${task.name} was cancelled with an exception" }
                } else logger.debug { "Task ${task.name} has finished running." }
                tasks.remove(task.taskID)
            }
        }
        job.start()
        return task
    }

    override fun activateTasks(plugin: MinixPlugin): IntArray? =
        tasks.filterValues { it.owner == plugin }.ifEmpty {
            null
        }?.keys?.toIntArray()

    override fun cancelTask(taskID: Int): Boolean {
        if (taskID < 0) return false
        val task = tasks[taskID] ?: return false
        val result = tasks.entries.removeIf { (_, task) ->
            task.taskID == taskID
        }
        return result.takeUnless { it } ?: task.cancel0()
    }

    override suspend fun shutdownTask(taskID: Int): Boolean {
        val task = tasks[taskID] ?: return false
        return task.keepRunning.getAndSet(false)
    }

    override fun isCurrentlyRunning(taskID: Int): Boolean =
        tasks[taskID]?.job?.isActive ?: false

    override fun runTask(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask
    ): CoroutineTask = handleStart(coroutineTask.sync() as CoroutineTaskImpl)

    override fun runTask(
        plugin: MinixPlugin,
        name: String?,
        task: CoroutineBlock
    ): CoroutineTask = handleStart(CoroutineTaskImpl(plugin, task))

    override fun runTask(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable
    ): CoroutineTask = handleStart(CoroutineTaskImpl(plugin, runnable))

    override fun runTaskLater(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Duration
    ): CoroutineTask = handleStart(coroutineTask.sync() as CoroutineTaskImpl, delay)

    override fun runTaskLater(
        plugin: MinixPlugin,
        name: String?,
        task: CoroutineBlock,
        delay: Duration
    ): CoroutineTask = handleStart(CoroutineTaskImpl(plugin, task), delay)

    override fun runTaskLater(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Duration
    ): CoroutineTask = handleStart(CoroutineTaskImpl(plugin, runnable), delay)

    override fun runTaskTimer(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Duration,
        period: Duration
    ): CoroutineTask = handleStart(coroutineTask.sync() as CoroutineTaskImpl, delay, period)

    override fun runTaskTimer(
        plugin: MinixPlugin,
        name: String?,
        task: CoroutineBlock,
        delay: Duration,
        period: Duration
    ): CoroutineTask = handleStart(CoroutineTaskImpl(plugin, task), delay, period)

    override fun runTaskTimer(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Duration,
        period: Duration
    ): CoroutineTask = handleStart(CoroutineTaskImpl(plugin, runnable), delay, period)

    override fun runAsyncTask(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask
    ): CoroutineTask = handleStart(coroutineTask.async() as CoroutineTaskImpl)

    override fun runAsyncTask(
        plugin: MinixPlugin,
        name: String?,
        task: CoroutineBlock
    ): CoroutineTask = handleStart(CoroutineTaskImpl(plugin, task).async())

    override fun runAsyncTask(
        plugin: MinixPlugin,
        name: String?,
        runnable: CoroutineRunnable
    ): CoroutineTask = handleStart(CoroutineTaskImpl(plugin, runnable).async())

    override fun runAsyncTaskLater(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Duration
    ): CoroutineTask = handleStart(coroutineTask.async() as CoroutineTaskImpl, delay)

    override fun runAsyncTaskLater(
        plugin: MinixPlugin,
        name: String?,
        task: CoroutineBlock,
        delay: Duration
    ): CoroutineTask = handleStart(CoroutineTaskImpl(plugin, task).async(), delay)

    override fun runAsyncTaskLater(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Duration
    ): CoroutineTask = handleStart(CoroutineTaskImpl(plugin, runnable).async(), delay)

    override fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Duration,
        period: Duration
    ): CoroutineTask = handleStart(coroutineTask.async() as CoroutineTaskImpl, delay, period)

    override fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        name: String?,
        task: CoroutineBlock,
        delay: Duration,
        period: Duration
    ): CoroutineTask = handleStart(CoroutineTaskImpl(plugin, task).async(), delay, period)

    override fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Duration,
        period: Duration
    ): CoroutineTask = handleStart(CoroutineTaskImpl(plugin, runnable).async(), delay, period)
}
