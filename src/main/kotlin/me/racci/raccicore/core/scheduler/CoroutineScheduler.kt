@file:Suppress("MemberVisibilityCanBePrivate")
package me.racci.raccicore.core.scheduler

import kotlinx.coroutines.*
import me.racci.raccicore.api.plugin.RacciPlugin
import me.racci.raccicore.api.scheduler.CoroutineRunnable
import me.racci.raccicore.api.scheduler.CoroutineScheduler
import me.racci.raccicore.api.scheduler.CoroutineTask
import me.racci.raccicore.api.utils.TimeConversionUtils.tickToMillisecond
import me.racci.raccicore.core.RacciCore
import org.bukkit.plugin.IllegalPluginAccessException
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.IntUnaryOperator
import kotlin.time.ExperimentalTime

@ApiStatus.Internal
@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
object CoroutineScheduler: CoroutineScheduler {

    /**
     * The [CoroutineScope] in which all [CoroutineScheduler] are handled.
     */
    val scope = CoroutineScope(RacciCore.asyncDispatcher)
    val bukkitScope = CoroutineScope(RacciCore.syncDispatcher)

    /**
     * Makes sure we don't go over the [Int.MAX_VALUE]
     */
    private val incrementID = IntUnaryOperator label@ {
        if(it == Int.MAX_VALUE) {
            return@label -1
        }
        it + 1
    }

    /**
     * Tracker for getting a unique [IntCoroutineTask.taskID] for each task.
     */
    private val ids = AtomicInteger(-1)

    /**
     * A Hashmap of the currently registered [IntCoroutineTask]'s that are running.
     */
    private val tasks: HashMap<Int, IntCoroutineTask> = HashMap()

    /**
     * Handle start
     *
     * @param task The [IntCoroutineTask]
     * @param delay The delay in ticks before the initial start.
     * @param period If not repeating -2 else how the delay in ticks between invokes.
     */
    private fun handleStart(
        task: IntCoroutineTask,
        delay: Long = 0L,
        period: Long = -2,
    ): CoroutineTask {
        // If this happens to you. Just stop it. Cleanup your shit before disabling
        if(!task.owner.isEnabled) {
            throw IllegalPluginAccessException("Plugin ${task.owner.description.fullName} attempted to register task while disabled")
        }
        val id = nextID()
        task.taskID = id
        val scope = if(task.async) scope else bukkitScope
        val job = scope.launch(start = CoroutineStart.LAZY) {
            // Wait before starting if there is a delay
            if(delay > 0) {
                delay(delay.tickToMillisecond)
            }
            // If this is a repeating task
            if(period > 0) {
                // Only calculate once
                task.period = period.tickToMillisecond
                while(!task.cancelled) {
                    task.runnable?.run() ?: task.task.invoke(task.owner to this)
                    delay(task.period)
                }
            } else {
                task.period = IntCoroutineTask.NO_REPEATING
                // Make sure it isn't cancelled the one time because some people are weird
                if(!task.cancelled) {
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
        } while(tasks.containsKey(id))
        return id
    }

    override suspend fun cancelTask(taskID: Int): Boolean {
        if(taskID <= 0) return false
        val task = tasks[taskID] ?: return false
        val result = tasks.entries.removeIf {(_, task)->
            if(task.taskID == taskID) {
                return@removeIf true
            } ; false
        }
        return if(result) {
            task.cancel0()
        } else false
    }

    override suspend fun cancelAllTasks(plugin: RacciPlugin) {
        val tasks = ArrayList<IntCoroutineTask>()
        this.tasks.entries.removeIf {(id, task)->
            if(task.taskID == id
               && task.owner == plugin
            ) {
                task.owner.log.debug("Cancelling task ${task.taskID}")
                tasks.add(task)
                return@removeIf true
            }
            false
        }
        tasks.forEach{it.cancel0()}
    }

    override fun isCurrentlyRunning(taskID: Int) =
        tasks[taskID]?.job?.isActive ?: false

    override fun runTask(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask
    ) = handleStart(coroutineTask.sync() as IntCoroutineTask)

    override fun runTask(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit
    ) = handleStart(IntCoroutineTask(plugin, task))

    override fun runTask(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable
    ) = handleStart(IntCoroutineTask(plugin, runnable))

    override fun runTaskLater(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask,
        delay: Long
    ) = handleStart(coroutineTask.sync() as IntCoroutineTask, delay)

    override fun runTaskLater(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit,
        delay: Long
    ) = handleStart(IntCoroutineTask(plugin, task), delay)

    override fun runTaskLater(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable,
        delay: Long
    ) = handleStart(IntCoroutineTask(plugin, runnable), delay)

    override fun runTaskTimer(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask,
        delay: Long,
        period: Long
    ) = handleStart(coroutineTask.sync() as IntCoroutineTask, delay, period)

    override fun runTaskTimer(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.()->Unit,
        delay: Long,
        period: Long,
    ) = handleStart(IntCoroutineTask(plugin, task), delay, period)

    override fun runTaskTimer(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
        period: Long
    ) = handleStart(IntCoroutineTask(plugin, runnable), delay, period)

    override fun runAsyncTask(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask
    ) = handleStart(coroutineTask.async() as IntCoroutineTask)

    override fun runAsyncTask(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit
    ) = handleStart(IntCoroutineTask(plugin, task).async())

    override fun runAsyncTask(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable
    ) = handleStart(IntCoroutineTask(plugin, runnable).async())

    override fun runAsyncTaskLater(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask,
        delay: Long
    ) = handleStart(coroutineTask.async() as IntCoroutineTask, delay)

    override fun runAsyncTaskLater(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit,
        delay: Long
    ) = handleStart(IntCoroutineTask(plugin, task).async(), delay)

    override fun runAsyncTaskLater(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable,
        delay: Long
    ) = handleStart(IntCoroutineTask(plugin, runnable).async(), delay)

    override fun runAsyncTaskTimer(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask,
        delay: Long,
        period: Long
    ) = handleStart(coroutineTask.async() as IntCoroutineTask, delay, period)

    override fun runAsyncTaskTimer(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.()->Unit,
        delay: Long,
        period: Long,
    ) = handleStart(IntCoroutineTask(plugin, task).async(), delay, period)

    override fun runAsyncTaskTimer(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
        period: Long
    ) = handleStart(IntCoroutineTask(plugin, runnable).async(), delay, period)

}