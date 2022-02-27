package dev.racci.minix.core.scheduler

import dev.racci.minix.api.coroutine.minecraftDispatcher
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.scheduler.CoroutineBlock
import dev.racci.minix.api.scheduler.CoroutineRunnable
import dev.racci.minix.api.scheduler.CoroutineScheduler
import dev.racci.minix.api.scheduler.CoroutineTask
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import org.koin.core.component.get
import java.lang.management.ManagementFactory
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class CoroutineSchedulerImpl(override val plugin: Minix) : Extension<Minix>(), CoroutineScheduler {

    override val name = "Coroutine Scheduler"
    override val bindToKClass: KClass<*> get() = CoroutineScheduler::class
    override val parentJob: CompletableJob by lazy { SupervisorJob() }

    private val threadContext = lazy {
        var threadCount = ManagementFactory.getThreadMXBean().threadCount / 4
        threadCount = when {
            threadCount < 1 -> 1
            threadCount > 4 -> 4
            else -> threadCount
        }
        newFixedThreadPoolContext(threadCount, "Coroutine Scheduler Threads")
    }
    private val bukkitContext by lazy { get<Minix>().minecraftDispatcher }
    private val ids by lazy { atomic(-1) }
    private val tasks by lazy { mutableMapOf<Int, CoroutineTaskImpl>() }

    override suspend fun handleEnable() {}

    @OptIn(InternalCoroutinesApi::class)
    private fun handleStart(
        task: CoroutineTaskImpl,
        delay: Duration? = null,
        period: Duration? = null,
    ): CoroutineTask {
        task.taskID = ids.incrementAndGet()
        task.period = period

        val context = if (task.async) threadContext.value else bukkitContext
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
                    log.error(throwable) { "Task ${task.name} was cancelled with an exception" }
                } else log.debug { "Task ${task.name} has finished running." }
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
        return if (result) task.cancel0() else false
    }

    override suspend fun shutdownTask(taskID: Int): Boolean {
        val task = tasks[taskID] ?: return false
        return task.keepRunning.getAndSet(false)
    }

    override fun isCurrentlyRunning(taskID: Int) =
        tasks[taskID]?.job?.isActive ?: false

    override fun runTask(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
    ) = handleStart(coroutineTask.sync() as CoroutineTaskImpl)

    override fun runTask(
        plugin: MinixPlugin,
        name: String?,
        task: CoroutineBlock,
    ) = handleStart(CoroutineTaskImpl(plugin, task))

    override fun runTask(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
    ) = handleStart(CoroutineTaskImpl(plugin, runnable))

    override fun runTaskLater(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Duration,
    ) = handleStart(coroutineTask.sync() as CoroutineTaskImpl, delay)

    override fun runTaskLater(
        plugin: MinixPlugin,
        name: String?,
        task: CoroutineBlock,
        delay: Duration,
    ) = handleStart(CoroutineTaskImpl(plugin, task), delay)

    override fun runTaskLater(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Duration,
    ) = handleStart(CoroutineTaskImpl(plugin, runnable), delay)

    override fun runTaskTimer(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Duration,
        period: Duration,
    ) = handleStart(coroutineTask.sync() as CoroutineTaskImpl, delay, period)

    override fun runTaskTimer(
        plugin: MinixPlugin,
        name: String?,
        task: CoroutineBlock,
        delay: Duration,
        period: Duration,
    ) = handleStart(CoroutineTaskImpl(plugin, task), delay, period)

    override fun runTaskTimer(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Duration,
        period: Duration,
    ) = handleStart(CoroutineTaskImpl(plugin, runnable), delay, period)

    override fun runAsyncTask(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
    ) = handleStart(coroutineTask.async() as CoroutineTaskImpl)

    override fun runAsyncTask(
        plugin: MinixPlugin,
        name: String?,
        task: CoroutineBlock,
    ) = handleStart(CoroutineTaskImpl(plugin, task).async())

    override fun runAsyncTask(
        plugin: MinixPlugin,
        name: String?,
        runnable: CoroutineRunnable,
    ) = handleStart(CoroutineTaskImpl(plugin, runnable).async())

    override fun runAsyncTaskLater(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Duration,
    ) = handleStart(coroutineTask.async() as CoroutineTaskImpl, delay)

    override fun runAsyncTaskLater(
        plugin: MinixPlugin,
        name: String?,
        task: CoroutineBlock,
        delay: Duration,
    ) = handleStart(CoroutineTaskImpl(plugin, task).async(), delay)

    override fun runAsyncTaskLater(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Duration,
    ) = handleStart(CoroutineTaskImpl(plugin, runnable).async(), delay)

    override fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Duration,
        period: Duration,
    ) = handleStart(coroutineTask.async() as CoroutineTaskImpl, delay, period)

    override fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        name: String?,
        task: CoroutineBlock,
        delay: Duration,
        period: Duration,
    ) = handleStart(CoroutineTaskImpl(plugin, task).async(), delay, period)

    override fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Duration,
        period: Duration,
    ) = handleStart(CoroutineTaskImpl(plugin, runnable).async(), delay, period)
}
