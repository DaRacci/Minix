@file:Suppress("MemberVisibilityCanBePrivate")

package dev.racci.minix.core.scheduler

import dev.racci.minix.api.coroutine.asyncDispatcher
import dev.racci.minix.api.coroutine.minecraftDispatcher
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.inWholeTicks
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.scheduler.CoroutineRunnable
import dev.racci.minix.api.scheduler.CoroutineScheduler
import dev.racci.minix.api.scheduler.CoroutineTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.bukkit.plugin.IllegalPluginAccessException
import org.koin.core.component.get
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.IntUnaryOperator
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
class CoroutineSchedulerImpl(override val plugin: Minix) : Extension<Minix>(), CoroutineScheduler {

    override val name = "Coroutine Scheduler"
    override val bindToKClass: KClass<*> get() = CoroutineScheduler::class
    override val scope by lazy { CoroutineScope(get<Minix>().asyncDispatcher) }

    val bukkitScope by lazy { CoroutineScope(get<Minix>().minecraftDispatcher) }

    private val ids by lazy { AtomicInteger(-1) }
    private val tasks: MutableMap<Int, CoroutineTaskImpl> by lazy(::mutableMapOf)

    override suspend fun handleEnable() {}

    private val incrementID = IntUnaryOperator {
        if (it == Int.MAX_VALUE) {
            -1
        } else it + 1
    }

    private fun handleStart(
        task: CoroutineTaskImpl,
        delay: Duration? = null,
        period: Duration? = null,
    ): CoroutineTask {
        // If this happens to you. Just stop it. Cleanup your shit before disabling
        if (!task.owner.isEnabled) {
            throw IllegalPluginAccessException("Plugin ${task.owner.description.fullName} attempted to register task while disabled")
        }
        val id = nextID()
        task.taskID = id
        val scope = if (task.async) scope else bukkitScope
        val job = scope.launch(start = CoroutineStart.LAZY) {
            log.debug { "Task number $id for ${task.owner.name} has started" }
            // Wait before starting if there is a delay
            delay?.let { delay(it) }
            log.debug { "Task number $id for ${task.owner.name} has waited for ${delay?.inWholeTicks} ticks" }
            // If this is a repeating task
            if (period != null) {
                task.period = period
                log.debug { "Task number $id for ${task.owner.name} is a repeating task with period ${period.inWholeTicks} ticks" }
                try {
                    while (!task.cancelled) {
                        withTimeout(5.seconds) { // We don't want to hang on the task forever
                            task.runnable?.run() ?: task.task.invoke(task.owner to this)
                        }
                        delay(task.period!!)
                    }
                } catch (e: Exception) {
                    task.cancel()
                    log.error(e) { "There was an error while running task $id for ${task.owner.name}" }
                }
            } else {
                task.period = null
                try {
                    if (!task.cancelled) {
                        task.runnable?.run() ?: task.task.invoke(task.owner to this)
                    }
                } catch (e: Exception) { log.error(e) { "There was an error while running task $id for ${task.owner.name}" } }
            }
        }
        task.job = job
        tasks[id] = task
        log.debug { "Task number $id for ${task.owner.name} has been registered" }
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

    override suspend fun activateTasks(plugin: MinixPlugin): IntArray? =
        tasks.filter { it.value.owner == plugin }.ifEmpty {
            null
        }?.keys?.toIntArray()

    override suspend fun cancelTask(taskID: Int): Boolean {
        if (taskID < 0) return false
        val task = tasks[taskID] ?: return false
        val result = tasks.entries.removeIf { (_, task) ->
            task.taskID == taskID
        }
        return if (result) {
            log.debug { "Task number $taskID for ${task.owner.name} has been cancelled" }
            task.cancel0()
        } else {
            log.debug { "Task number $taskID did not match any registered tasks" }
            false
        }
    }

    override suspend fun cancelTask(name: String): Boolean {
        tasks.entries.find { it.value.name == name }?.let {
            return cancelTask(it.key)
        }
        return false
    }

    override suspend fun cancelAllTasks(plugin: MinixPlugin) {
        val tasks = ArrayList<CoroutineTaskImpl>()
        this.tasks.entries.removeIf { (id, task) ->
            if (task.taskID == id &&
                task.owner == plugin
            ) {
                task.owner.log.debug("Cancelling task ${task.taskID}")
                tasks.add(task)
                return@removeIf true
            }
            false
        }
        tasks.forEach { it.cancel0() }
    }

    override fun isCurrentlyRunning(taskID: Int) =
        tasks[taskID]?.job?.isActive ?: false

    override fun isCurrentlyRunning(name: String) =
        tasks.entries.find { it.value.name == name }?.value?.job?.isActive ?: false

    override fun runTask(
        plugin: MinixPlugin,
        coroutineTask: CoroutineTask,
    ) = handleStart(coroutineTask.sync() as CoroutineTaskImpl)

    override fun runTask(
        plugin: MinixPlugin,
        name: String?,
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
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
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
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
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
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
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
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
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
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
        task: Pair<MinixPlugin, CoroutineScope>.() -> Unit,
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
