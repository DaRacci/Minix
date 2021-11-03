package me.racci.raccicore.runnables

import com.github.shynixn.mccoroutine.asyncDispatcher
import com.github.shynixn.mccoroutine.minecraftDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.racci.raccicore.RacciCore
import me.racci.raccicore.RacciPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.ApiStatus

//
///**
// * Bukkit Task handler.
// */
//sealed interface KotlinRunnable : Runnable {
//    /**
//     * Run the task.
//     *
//     * @return The created {@link BukkitTask}.
//     */
//    fun runTask() : BukkitTask
//
//    /**
//     * Run the task asynchronously.
//     *
//     * @return The created {@link BukkitTask}
//     */
//    fun runTaskAsynchronously() : BukkitTask
//
//    /**
//     * Run the task after a specified number of ticks.
//     *
//     * @param delay The number of ticks to wait.
//     * @return The created {@link BukkitTask}
//     */
//    fun runTaskLater(delay: Long) : BukkitTask
//
//    /**
//     * Run the task asynchronously after a specified number of ticks.
//     *
//     * @param delay The number of ticks to wait.
//     * @return The created {@link BukkitTask}
//     */
//    fun runTaskLaterAsynchronously(delay: Long) : BukkitTask
//
//    /**
//     * Run the task repeatedly on a timer.
//     *
//     * @param delay  The delay before the task is first ran (in ticks).
//     * @param period The ticks elapsed before the task is run again.
//     * @return The created {@link BukkitTask}
//     */
//    fun runTaskTimer(delay: Long, period: Long) : BukkitTask
//
//    /**
//     * Run the task repeatedly on a timer asynchronously.
//     *
//     * @param delay  The delay before the task is first ran (in ticks).
//     * @param period The ticks elapsed before the task is run again.
//     * @return The created {@link BukkitTask}
//     */
//    fun runTaskTimerAsynchronously(delay: Long, period: Long) : BukkitTask
//
//    /**
//     * Cancel the task.
//     */
//    fun cancel();
//}
//
///**
// * Thread scheduler to handle tasks and asynchronous code.
// */
//interface Scheduler {
//    /**
//     * Run the task after a specified tick delay.
//     *
//     * @param runnable   The lambda to run.
//     * @param ticksLater The amount of ticks to wait before execution.
//     * @return The created [BukkitTask].
//     */
//    fun runLater(
//        runnable: Runnable,
//        ticksLater: Long
//    ): BukkitTask
//
//    /**
//     * Run the task repeatedly on a timer.
//     *
//     * @param runnable The lambda to run.
//     * @param delay    The amount of ticks to wait before the first execution.
//     * @param repeat   The amount of ticks to wait between executions.
//     * @return The created [BukkitTask].
//     */
//    fun runTimer(
//        runnable: Runnable,
//        delay: Long,
//        repeat: Long
//    ): BukkitTask
//
//    /**
//     * Run the task repeatedly and asynchronously on a timer.
//     *
//     * @param runnable The lambda to run.
//     * @param delay    The amount of ticks to wait before the first execution.
//     * @param repeat   The amount of ticks to wait between executions.
//     * @return The created [BukkitTask].
//     */
//    fun runAsyncTimer(
//        runnable: Runnable,
//        delay: Long,
//        repeat: Long
//    ): BukkitTask
//
//    /**
//     * Run the task.
//     *
//     * @param runnable The lambda to run.
//     * @return The created [BukkitTask].
//     */
//    fun run(runnable: Runnable): BukkitTask
//
//    /**
//     * Run the task asynchronously.
//     *
//     * @param runnable The lambda to run.
//     * @return The created [BukkitTask].
//     */
//    fun runAsync(runnable: Runnable): BukkitTask
//
//    /**
//     * Schedule the task to be ran repeatedly on a timer.
//     *
//     * @param runnable The lambda to run.
//     * @param delay    The amount of ticks to wait before the first execution.
//     * @param repeat   The amount of ticks to wait between executions.
//     * @return The id of the task.
//     */
//    fun syncRepeating(
//        runnable: Runnable,
//        delay: Long,
//        repeat: Long
//    ): Int
//
//    /**
//     * Cancel all running tasks from the linked [me.racci.raccicore.RacciPlugin].
//     */
//    fun cancelAll()
//}
abstract class KotlinRunnable(
    val plugin: RacciPlugin,
    val async: Boolean = false,
    val repeating: Boolean = false,
    val delay: Long = 0L,
    val period: Long = 20L,
) : BukkitRunnable() {

    fun start() {
        if(repeating) {
            if(async) {
                this.runTaskTimerAsynchronously(plugin, delay, period)
            } else {
                this.runTaskTimer(plugin, delay, period)
            }
        } else {
            if(async) {
                this.runTaskAsynchronously(plugin)
            } else {
                this.runTask(plugin)
            }
        }
    }
}

suspend fun RacciPlugin.runnable(
    async       : Boolean = false,
    repeating   : Boolean = false,
    delay       : Long = 0L,
    period      : Long = 0L,
    unit        : Unit.() -> Unit
) = withContext(if(async) asyncDispatcher else minecraftDispatcher)
{
//    while()

    delay(delay)

}

/**
 * Create a new Constant running task.
 *
 * @param plugin the plugin which owns the task
 * @param async if the task will be executed on the main Bukkit thread or not
 * @param period how often the task is run in ticks (Second/1000)
 * @param task the unit which will be run during the task
 */
@ApiStatus.Experimental
class ConstantTask(
    val plugin  : RacciPlugin,
    val async   : Boolean,
    val period  : Long,
    val task    : CoroutineScope.() -> Unit
) {

    private var job: Job = CoroutineScope(if(async) plugin.asyncDispatcher else plugin.minecraftDispatcher).launch {
        while(this.isActive) {
            task
            delay(period*1000)
        }
    }

//    private testJob

    fun init() {
        job.start()
    }

    suspend fun close() {
        job.
        job.cancelAndJoin()
    }
}

fun testTask() {

    ConstantTask(RacciCore.instance, true, 20) {

    }

}