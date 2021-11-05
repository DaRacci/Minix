package me.racci.raccicore.skedule

import me.racci.raccicore.RacciPlugin
import org.bukkit.scheduler.BukkitScheduler
import org.jetbrains.annotations.ApiStatus
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume

@Deprecated("Deprecated in favour of MCCoroutine", ReplaceWith(""))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
fun RacciPlugin.schedule(
    initialContext: SynchronizationContext = SynchronizationContext.SYNC,
    co: suspend BukkitSchedulerController.() -> Unit
): CoroutineTask {
    return server.scheduler.schedule(this, initialContext, co)
}

/**
 * Schedule a coroutine with the Bukkit Scheduler.
 *
 * @receiver The BukkitScheduler instance to use for scheduling tasks.
 * @param plugin The Plugin instance to use for scheduling tasks.
 * @param initialContext The initial synchronization context to start off the coroutine with. See
 * [SynchronizationContext].
 *
 * @see SynchronizationContext
 */
@Deprecated("Deprecated in favour of MCCoroutine", ReplaceWith(""))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
fun BukkitScheduler.schedule(
    plugin: RacciPlugin,
    initialContext: SynchronizationContext = SynchronizationContext.SYNC,
    co: suspend BukkitSchedulerController.() -> Unit
): CoroutineTask {
    val controller = BukkitSchedulerController(plugin, this)
    val block: suspend BukkitSchedulerController.() -> Unit = {
        try {
            start(initialContext)
            co()
        } finally {
            cleanup()
        }
    }

    block.createCoroutine(receiver = controller, completion = controller).resume(Unit)

    return CoroutineTask(controller)
}

/**
 * Sugar functions to allow for easier creation of coroutines.
 *
 * For example, before:
 * ```kotlin
 * Bukkit.getScheduler().schedule(myPlugin) {
 *     //...
 * }
 * ```
 * After:
 * ```kotlin
 * skeduleSync(myPlugin) {
 *     //...
 * }
 * ```
 */
@Deprecated("Deprecated in favour of MCCoroutine", ReplaceWith(""))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
fun skeduleSync(plugin: RacciPlugin, block: suspend BukkitSchedulerController.() -> Unit): CoroutineTask {
    return bukkitScheduler.schedule(plugin, SynchronizationContext.SYNC, block)
}

/**
 * Sugar functions to allow for easier creation of coroutines.
 *
 * For example, before:
 * ```kotlin
 * Bukkit.getScheduler().schedule(myPlugin, SynchronizationContext.ASYNC) {
 *     //...
 * }
 * ```
 * After:
 * ```kotlin
 * skeduleAsync(myPlugin) {
 *     //...
 * }
 * ```
 */
@Deprecated("Deprecated in favour of MCCoroutine", ReplaceWith(""))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
fun skeduleAsync(plugin: RacciPlugin, block: suspend BukkitSchedulerController.() -> Unit): CoroutineTask {
    return bukkitScheduler.schedule(plugin, SynchronizationContext.ASYNC, block)
}