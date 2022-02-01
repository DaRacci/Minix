package dev.racci.minix.api.coroutine

import dev.racci.minix.api.coroutine.contract.Coroutine
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.coroutine.impl.CoroutineImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.bukkit.command.PluginCommand
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import kotlin.coroutines.CoroutineContext

/**
 * Static session.
 */
internal val coroutine: Coroutine by lazy {
    CoroutineImpl()
//    Class.forName("dev.racci.minix.core.coroutine.CoroutineImpl")
//        .newInstance() as Coroutine
}

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
    get() = coroutine.getCoroutineSession(this).dispatcherMinecraft

/**
 * Gets the plugin async dispatcher.
 */
val MinixPlugin.asyncDispatcher: CoroutineContext
    get() = coroutine.getCoroutineSession(this).dispatcherAsync

/**
 * Gets the plugin coroutine scope.
 */
val MinixPlugin.scope: CoroutineScope
    get() = coroutine.getCoroutineSession(this).scope

/**
 * Launches the given function in the Coroutine Scope of the given plugin.
 * This function may be called immediately without any delay if the Thread
 * calling this function Bukkit.isPrimaryThread() is true. This means
 * for example that event cancelling or modifying return values is still possible.
 * @param dispatcher Coroutine context. The default context is minecraft dispatcher.
 * @param f callback function inside a coroutine scope.
 * @return Cancelable coroutine job.
 */
fun MinixPlugin.launch(
    dispatcher: CoroutineContext,
    f: suspend CoroutineScope.() -> Unit,
): Job =
    coroutine.getCoroutineSession(this).launch(dispatcher, f)

/**
 * Launches the given function in the Coroutine Scope of the given plugin.
 * This function may be called immediately without any delay if the Thread
 * calling this function Bukkit.isPrimaryThread() is true. This means
 * for example that event cancelling or modifying return values is still possible.
 * @param f callback function inside a coroutine scope.
 * @return Cancelable coroutine job.
 */
fun MinixPlugin.launch(f: suspend CoroutineScope.() -> Unit): Job =
    coroutine.getCoroutineSession(this).launch(minecraftDispatcher, f)

/**
 * Launches the given function in the Coroutine Scope of the given plugin async.
 * This function may be called immediately without any delay if the Thread
 * calling this function Bukkit.isPrimaryThread() is false. This means
 * for example that event cancelling or modifying return values is still possible.
 * @param f callback function inside a coroutine scope.
 * @return Cancelable coroutine job.
 */
fun MinixPlugin.launchAsync(f: suspend CoroutineScope.() -> Unit): Job =
    coroutine.getCoroutineSession(this).launch(this.asyncDispatcher, f)

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
) = coroutine.getCoroutineSession(plugin).eventService.registerSuspendListener(listener)

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
): Collection<Job> = coroutine.getCoroutineSession(plugin).eventService.fireSuspendingEvent(event)

/**
 * Registers an command executor with suspending function.
 * Does exactly the same as PluginCommand.setExecutor.
 */
fun PluginCommand.setSuspendingExecutor(
    suspendingCommandExecutor: SuspendingCommandExecutor
) = coroutine.getCoroutineSession(plugin as MinixPlugin).commandService.registerSuspendCommandExecutor(
    this,
    suspendingCommandExecutor
)

/**
 * Registers a tab completer with suspending function.
 * Does exactly the same as PluginCommand.setExecutor.
 */
fun PluginCommand.setSuspendingTabCompleter(suspendingTabCompleter: SuspendingTabCompleter) =
    coroutine.getCoroutineSession(plugin as MinixPlugin).commandService.registerSuspendTabCompleter(
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
