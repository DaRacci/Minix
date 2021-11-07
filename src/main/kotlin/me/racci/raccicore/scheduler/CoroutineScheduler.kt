package me.racci.raccicore.scheduler

import com.github.shynixn.mccoroutine.asyncDispatcher
import com.github.shynixn.mccoroutine.minecraftDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import me.racci.raccicore.RacciCore
import me.racci.raccicore.RacciPlugin
import me.racci.raccicore.utils.TimeConversionUtils.millisecondToTick
import me.racci.raccicore.utils.now
import org.bukkit.plugin.IllegalPluginAccessException
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.IntUnaryOperator
import kotlin.time.ExperimentalTime

@ApiStatus.Experimental
@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
object CoroutineScheduler: ICoroutineScheduler {

    /**
     * The [CoroutineScope] in which all [CoroutineScheduler] are handled.
     */
    val scope = CoroutineScope(RacciCore.instance.asyncDispatcher)
    val bukkitScope = CoroutineScope(RacciCore.instance.minecraftDispatcher)

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
     * Tracker for getting a unique [CoroutineTask.taskID] for each task.
     */
    private val ids = AtomicInteger(-1)

    /**
     * A Hashmap of the currently registered [CoroutineTask]'s that are running.
     */
    private val tasks: HashMap<Int, CoroutineTask> = HashMap()

    /**
     * Handle start
     *
     * @param task The [CoroutineTask]
     * @param delay The delay in ticks before the initial start.
     * @param period If not repeating -2 else how the delay in ticks between invokes.
     */
    private fun handleStart(
        task: CoroutineTask,
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
                delay(delay.millisecondToTick)
            }
            // If this is a repeating task
            if(period > 0) {
                // Only calculate once
                task.period = period.millisecondToTick
                var last = now().toEpochMilliseconds()
                while(!task.cancelled) {
                    task.owner.log.debug("${now().toEpochMilliseconds() - last}")
                    task.runnable?.run() ?: task.task.invoke(task.owner to this)
                    last = now().toEpochMilliseconds()
                    delay(task.period)
                }
            } else {
                task.period = CoroutineTask.NO_REPEATING
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
        val tasks = ArrayList<CoroutineTask>()
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
    ): ITask = handleStart(coroutineTask.sync())

    override fun runTask(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit
    ): ITask = handleStart(CoroutineTask(plugin, task))

    override fun runTask(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable
    ): ITask = handleStart(CoroutineTask(plugin, runnable))

    override fun runTaskLater(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask,
        delay: Long
    ): ITask = handleStart(coroutineTask.sync(), delay)

    override fun runTaskLater(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit,
        delay: Long
    ): ITask = handleStart(CoroutineTask(plugin, task), delay)

    override fun runTaskLater(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable,
        delay: Long
    ): ITask = handleStart(CoroutineTask(plugin, runnable), delay)

    override fun runTaskTimer(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask,
        delay: Long,
        period: Long
    ): ITask = handleStart(coroutineTask.sync(), delay, period)

    override fun runTaskTimer(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.()->Unit,
        delay: Long,
        period: Long,
    ): ITask = handleStart(CoroutineTask(plugin, task), delay, period)

    override fun runTaskTimer(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
        period: Long
    ): ITask = handleStart(CoroutineTask(plugin, runnable), delay, period)

    override fun runAsyncTask(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask
    ): ITask = handleStart(coroutineTask.async())

    override fun runAsyncTask(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit
    ): ITask = handleStart(CoroutineTask(plugin, task).async())

    override fun runAsyncTask(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable
    ): ITask = handleStart(CoroutineTask(plugin, runnable).async())

    override fun runAsyncTaskLater(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask,
        delay: Long
    ): ITask = handleStart(coroutineTask.async(), delay)

    override fun runAsyncTaskLater(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.() -> Unit,
        delay: Long
    ): ITask = handleStart(CoroutineTask(plugin, task).async(), delay)

    override fun runAsyncTaskLater(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable,
        delay: Long
    ): ITask = handleStart(CoroutineTask(plugin, runnable).async(), delay)

    override fun runAsyncTaskTimer(
        plugin: RacciPlugin,
        coroutineTask: CoroutineTask,
        delay: Long,
        period: Long
    ): ITask = handleStart(coroutineTask.async(), delay, period)

    override fun runAsyncTaskTimer(
        plugin: RacciPlugin,
        task: Pair<RacciPlugin, CoroutineScope>.()->Unit,
        delay: Long,
        period: Long,
    ): ITask = handleStart(CoroutineTask(plugin, task).async(), delay, period)

    override fun runAsyncTaskTimer(
        plugin: RacciPlugin,
        runnable: CoroutineRunnable,
        delay: Long,
        period: Long
    ): ITask = handleStart(CoroutineTask(plugin, runnable).async(), delay, period)

}