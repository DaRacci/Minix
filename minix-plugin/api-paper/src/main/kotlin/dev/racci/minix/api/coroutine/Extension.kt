package dev.racci.minix.api.coroutine

import dev.racci.minix.api.data.enums.EventExecutionType
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.getKoin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import kotlin.coroutines.CoroutineContext

private val pluginService by getKoin().inject<PluginService>()

/**
 * Gets the plugin minecraft context.
 */
public val MinixPlugin.minecraftDispatcher: CoroutineContext
    get() = pluginService.coroutineSession[this].minecraftContext

/**
 * Launches the given function in the Coroutine Scope of the given plugin async.
 * This function may be called immediately without any delay if the Thread
 * calling this function Bukkit.isPrimaryThread() is false.
 * This means, for example, that event cancelling or modifying return values is still possible.
 * @param block callback function inside a coroutine scope.
 * @return Cancelable coroutine job.
 */
public fun MinixPlugin.launchAsync(
    block: suspend CoroutineScope.() -> Unit
): Job = pluginService.coroutineSession[this].launch(this.context, block = block)

/**
 * Registers an event listener with suspending functions.
 * Does the same thing as PluginService.registerEvents but makes suspension functions
 * possible.
 * Example:
 * ```
 * class MyPlayerJoinListener : Listener{
 *     @EventHandler
 *     suspend fun onPlayerJoinEvent(event: PlayerJoinEvent) {
 *
 *     }
 * }
 * ```
 * @param listener Bukkit Listener.
 * @param plugin Bukkit Plugin.
 */
public suspend fun PluginManager.registerSuspendingEvents(
    listener: Listener,
    plugin: MinixPlugin
): Unit = pluginService.coroutineSession[plugin].registerSuspendedListener(listener)

public suspend fun MinixPlugin.registerSuspendingEvents(
    listener: Listener
): Unit = pluginService.coroutineSession[this].registerSuspendedListener(listener)

/**
 * Calls an event with the given details.
 * Allows awaiting the completion of suspending event listeners.
 * * @param event Event details.
 * @param plugin Plugin plugin.
 * @param executionType Allows specifying how suspend receivers are run.
 * @return Collection of awaitable jobs, This job list may be empty if no suspending listener was called.
 * Each job instance represents an awaitable job for each method being called in each suspended listener.
 * For awaiting use callSuspendingEvent(..).joinAll().
 */
public fun PluginManager.callSuspendingEvent(
    event: Event,
    plugin: MinixPlugin,
    executionType: EventExecutionType
): Collection<Job> = pluginService.coroutineSession[plugin].fireSuspendingEvent(event, executionType)
