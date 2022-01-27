@file:Suppress("MemberVisibilityCanBePrivate")

package dev.racci.minix.core.scheduler

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.scheduler.CoroutineRunnable
import dev.racci.minix.api.scheduler.CoroutineScheduler
import dev.racci.minix.api.scheduler.CoroutineTask
import dev.racci.minix.api.utils.TimeConversionUtils.tickToMillisecond
import dev.racci.minix.core.Minix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.plugin.IllegalPluginAccessException
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.IntUnaryOperator
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
object CoroutineSchedulerImpl : CoroutineScheduler {

    init {
        val inst = CoroutineScheduler.Companion::class.java.getDeclaredField("instance")
        inst.isAccessible = true
        inst.set(CoroutineScheduler.Companion, this)
        inst.isAccessible = false
    }

    /**
     * The [CoroutineScope] in which all [CoroutineSchedulerImpl] are handled.
     */
    override val scope = CoroutineScope(Minix.asyncDispatcher)
    val bukkitScope = CoroutineScope(Minix.syncDispatcher)

    /**
     * Makes sure we don't go over the [Int.MAX_VALUE]
     */
    private val incrementID = IntUnaryOperator {
        if (it == Int.MAX_VALUE) {
            -1
        } else it + 1
    }

    /**
     * Tracker for getting a unique [CoroutineTaskImpl.taskID] for each task.
     */
    private val ids = AtomicInteger(-1)

    /**
     * A Hashmap of the currently registered [CoroutineTaskImpl]'s that are running.
     */
    private val tasks: HashMap<Int, CoroutineTaskImpl> = HashMap()

    /**
     * Handle start.
     *
     * @param task The [CoroutineTaskImpl]
     * @param delay The delay in ticks before the initial start.
     * @param period If not repeating -2 else how the delay in ticks between invokes.
     */
    private fun handleStart(
        task: CoroutineTaskImpl,
        delay: Long = 0L,
        period: Long = -2,
    ): CoroutineTask {
        // If this happens to you. Just stop it. Cleanup your shit before disabling
        if (!task.owner.isEnabled) {
            throw IllegalPluginAccessException("Plugin ${task.owner.description.fullName} attempted to register task while disabled")
        }
        val id = nextID()
        task.taskID = id
        val scope = if (task.async) scope else bukkitScope
        val job = scope.launch(start = CoroutineStart.LAZY) {
            // Wait before starting if there is a delay
            if (delay > 0) {
                delay(delay.tickToMillisecond)
            }
            // If this is a repeating task
            if (period > 0) {
                // Only calculate once
                task.period = period.tickToMillisecond
                while (!task.cancelled) {
                    task.runnable?.run() ?: task.task.invoke(task.owner to this)
                    delay(task.period)
                }
            } else {
                task.period = CoroutineTaskImpl.NO_REPEATING
                // Make sure it isn't cancelled the one time because some people are weird
                if (!task.cancelled) {
                    task.runnable?.run() ?: task.task.invoke(task.owner to this)
                }
            }
        }
        task.job = job
        tasks[id] = task
        job.start()
        return task
    }

    private fun nextID(): Int {
        var id: Int
        do {
            id = ids.updateAndGet(incrementID)
        } while (tasks.containsKey(id))
        return id
    }

    override suspend fun cancelTask(taskID: Int): Boolean {
        if (taskID <= 0) return false
        val task = tasks[taskID] ?: return false
        val result = tasks.entries.removeIf { (_, task) ->
            if (task.taskID == taskID) {
                return@removeIf true
            }; false
        }
        return if (result) {
            task.cancel0()
        } else false
    }

    override suspend fun cancelAllTasks(plugin: MinixPlugin) {
        val tasks = ArrayList<CoroutineTaskImpl>()
        this.tasks.entries.removeIf { (id, task) ->
            if (task.taskID == id &&
                task.owner == plugin
            ) {
                task.owner.log.info("Cancelling task ${task.taskID}")
                tasks.add(task)
                return@removeIf true
            }
            false
        }
        tasks.forEach { it.cancel0() }
    }

    override fun isCurrentlyRunning(taskID: Int) =
        tasks[taskID]?.job?.isActive ?: false

    override fun runTask(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
    ) = handleStart(coroutineTask.sync() as CoroutineTaskImpl)

    override fun runTask(
        plugin: MinixPlugin,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
    ) = handleStart(CoroutineTaskImpl(plugin, task))

    override fun runTask(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
    ) = handleStart(CoroutineTaskImpl(plugin, runnable))

    override fun runTaskLater(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Long,
    ) = handleStart(coroutineTask.sync() as CoroutineTaskImpl, delay)

    override fun runTaskLater(
        plugin: MinixPlugin,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
        delay: Long,
    ) = handleStart(CoroutineTaskImpl(plugin, task), delay)

    override fun runTaskLater(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
    ) = handleStart(CoroutineTaskImpl(plugin, runnable), delay)

    override fun runTaskTimer(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Long,
        period: Long,
    ) = handleStart(coroutineTask.sync() as CoroutineTaskImpl, delay, period)

    override fun runTaskTimer(
        plugin: MinixPlugin,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
        delay: Long,
        period: Long,
    ) = handleStart(CoroutineTaskImpl(plugin, task), delay, period)

    override fun runTaskTimer(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
        period: Long,
    ) = handleStart(CoroutineTaskImpl(plugin, runnable), delay, period)

    override fun runAsyncTask(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
    ) = handleStart(coroutineTask.async() as CoroutineTaskImpl)

    override fun runAsyncTask(
        plugin: MinixPlugin,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
    ) = handleStart(CoroutineTaskImpl(plugin, task).async())

    override fun runAsyncTask(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
    ) = handleStart(CoroutineTaskImpl(plugin, runnable).async())

    override fun runAsyncTaskLater(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Long,
    ) = handleStart(coroutineTask.async() as CoroutineTaskImpl, delay)

    override fun runAsyncTaskLater(
        plugin: MinixPlugin,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
        delay: Long,
    ) = handleStart(CoroutineTaskImpl(plugin, task).async(), delay)

    override fun runAsyncTaskLater(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
    ) = handleStart(CoroutineTaskImpl(plugin, runnable).async(), delay)

    override fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
        delay: Long,
        period: Long,
    ) = handleStart(coroutineTask.async() as CoroutineTaskImpl, delay, period)

    override fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
        delay: Long,
        period: Long,
    ) = handleStart(CoroutineTaskImpl(plugin, task).async(), delay, period)

    override fun runAsyncTaskTimer(
        plugin: MinixPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
        period: Long,
    ) = handleStart(CoroutineTaskImpl(plugin, runnable).async(), delay, period)
}
