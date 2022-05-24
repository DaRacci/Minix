@file:Suppress("unused")

package dev.racci.minix.api.coroutine

import dev.racci.minix.api.coroutine.contract.CoroutineService
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.getKoin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.bukkit.command.PluginCommand
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import org.jetbrains.annotations.ApiStatus
import kotlin.coroutines.CoroutineContext

/**
 * Static session.
 */
@get:ApiStatus.Internal
val coroutineService by getKoin().inject<CoroutineService>()

private var serverVersionInternal: String? = null

/**
 * Gets the server NMS version.
 */
val MinixPlugin.serverVersion: String
    get() {
        if (serverVersionInternal == null) {
            serverVersionInternal = server.javaClass.getPackage().name.replace(".", ",").split(",")[3]
        }

        return serverVersionInternal!!
    }

/**
 * Gets the plugin minecraft dispatcher.
 */
val MinixPlugin.minecraftDispatcher: CoroutineContext
    get() = coroutineService.getCoroutineSession(this).dispatcherMinecraft

/**
 * Gets the plugin async dispatcher.
 */
val MinixPlugin.asyncDispatcher: CoroutineContext
    get() = coroutineService.getCoroutineSession(this).dispatcherAsync

/**
 * Gets the plugin coroutine scope.
 */
val MinixPlugin.scope: CoroutineScope
    get() = coroutineService.getCoroutineSession(this).scope

/**
 * Launches the given function in the Coroutine Scope of the given plugin.
 * This function may be called immediately without any delay if the Thread
 * calling this function Bukkit.isPrimaryThread() is true. This means
 * for example that event cancelling or modifying return values is still possible.
 * @param dispatcher Coroutine context. The default context is minecraft dispatcher.
 * @param parentScope Parent job. The default is to just launch within the plugins scope.
 * @param f callback function inside a coroutine scope.
 * @return Cancelable coroutine job.
 */
fun MinixPlugin.launch(
    dispatcher: CoroutineContext = minecraftDispatcher,
    parentScope: CoroutineScope? = null,
    f: suspend CoroutineScope.() -> Unit
): Job = coroutineService.getCoroutineSession(this).launch(dispatcher, parentScope, f)

/**
 * Launches the given function in the Coroutine Scope of the given plugin async.
 * This function may be called immediately without any delay if the Thread
 * calling this function Bukkit.isPrimaryThread() is false. This means
 * for example that event cancelling or modifying return values is still possible.
 * @param f callback function inside a coroutine scope.
 * @return Cancelable coroutine job.
 */
fun MinixPlugin.launchAsync(f: suspend CoroutineScope.() -> Unit): Job =
    coroutineService.getCoroutineSession(this).launch(this.asyncDispatcher, null, f)

/**
 * Registers an event listener with suspending functions.
 * Does exactly the same thing as PluginService.registerEvents but makes suspension functions
 * possible.
 * Example:
 *
 * class MyPlayerJoinListener : Listener{
 *     @EventHandler
 *     suspend fun onPlayerJoinEvent(event: PlayerJoinEvent) {
 *
 *     }
 * }
 *
 * @param listener Bukkit Listener.
 * @param plugin Bukkit Plugin.
 */
fun PluginManager.registerSuspendingEvents(
    listener: Listener,
    plugin: MinixPlugin
) = coroutineService.getCoroutineSession(plugin).eventService.registerSuspendListener(listener)

fun MinixPlugin.registerSuspendingEvents(
    listener: Listener
) = coroutineService.getCoroutineSession(this).eventService.registerSuspendListener(listener)

/**
 * Calls an event with the given details.
 * Allows to await the completion of suspending event listeners.
 *
 * @param event Event details.
 * @param plugin Plugin plugin.
 * @return Collection of available jobs. This job list may be empty if no suspending listener
 * was called. Each job instance represents an available job for each method being called in each suspending listener.
 * For awaiting use callSuspendingEvent(..).joinAll().
 */
fun PluginManager.callSuspendingEvent(
    event: Event,
    plugin: MinixPlugin
): Collection<Job> = coroutineService.getCoroutineSession(plugin).eventService.fireSuspendingEvent(event)

/**
 * Registers an command executor with suspending function.
 * Does exactly the same as PluginCommand.setExecutor.
 */
fun PluginCommand.setSuspendingExecutor(
    suspendingCommandExecutor: SuspendingCommandExecutor
) = coroutineService.getCoroutineSession(plugin as MinixPlugin).commandService.registerSuspendCommandExecutor(
    this,
    suspendingCommandExecutor
)

/**
 * Registers a tab completer with suspending function.
 * Does exactly the same as PluginCommand.setExecutor.
 */
fun PluginCommand.setSuspendingTabCompleter(suspendingTabCompleter: SuspendingTabCompleter) =
    coroutineService.getCoroutineSession(plugin as MinixPlugin).commandService.registerSuspendTabCompleter(
        this,
        suspendingTabCompleter
    )

/**
 * Finds the version compatible class.
 */
fun MinixPlugin.findClazz(name: String): Class<*> = Class.forName(
    name.replace(
        "VERSION",
        serverVersion
    )
)
