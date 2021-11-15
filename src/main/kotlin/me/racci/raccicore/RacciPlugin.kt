@file:Suppress("unused", "UNUSED_EXPRESSION")
package me.racci.raccicore

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import com.github.shynixn.mccoroutine.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.registerSuspendingEvents
import me.racci.raccicore.core.PluginManager
import me.racci.raccicore.extensions.KotlinListener
import me.racci.raccicore.extensions.coloured
import me.racci.raccicore.extensions.pm
import me.racci.raccicore.lifecycle.Lifecycle
import me.racci.raccicore.lifecycle.LifecycleEvent
import me.racci.raccicore.lifecycle.LifecycleListener
import me.racci.raccicore.scheduler.CoroutineScheduler
import me.racci.raccicore.utils.UpdateChecker
import org.bukkit.plugin.java.JavaPlugin
import kotlin.properties.Delegates

/**
 * The superclass replacing [JavaPlugin],
 * This class provides many more features and allows implementation into the
 * systems of RacciCore.
 *
 * @property prefix The prefix for logging in console, this supports colours.
 * @property spigotId The ID of your plugin on Spigot.
 * @property bStatsId The ID of your plugin on bStats.
 */
abstract class RacciPlugin(
    val prefix  : String = "",
    val spigotId: Int    = 0,
    val bStatsId: Int    = 0,
) : SuspendingJavaPlugin() {

    val lifecycleListeners = HashSet<Lifecycle>()
    private val lifecycleLoadOrder
        get() = lifecycleListeners.sortedByDescending{it.priority}
    private val lifecycleDisableOrder
        get() = lifecycleListeners.sortedBy{it.priority}


    val log = Log(prefix.coloured())
    var commandManager by Delegates.notNull<PaperCommandManager>() ; private set

    /**
     * **DO NOT OVERRIDE**
     */
    override suspend fun onLoadAsync() {
        handleLoad()
        lifecycleLoadOrder.forEach{it.listener(LifecycleEvent.LOAD)}
    }

    /**
     * **DO NOT OVERRIDE**
     */
    override suspend fun onEnableAsync() {
        log.info("")
        log.info("Loading ${this.name}")
        log.info("")

        commandManager = PaperCommandManager(this)
        PluginManager::add

        if(spigotId != 0) ::UpdateChecker
        //if(bStatsId != 0) {}//TODO

        // Allow the plugin to do its thing before we register events etc.
        handleEnable()

        registerLifecycles().map{Lifecycle(it.second, it.first)}.forEach(lifecycleListeners::add)
        lifecycleLoadOrder.forEach{it.listener(LifecycleEvent.ENABLE)}
        registerListeners().forEach{pm.registerSuspendingEvents(it, this)}
        registerCommands().forEach(commandManager::registerCommand)

        log.info("")
        log.success("Finished Loading ${this.name}")
        log.info("")

        log.debug("HandleAfterLoad Started")
        handleAfterLoad()

    }

    suspend fun reload() {
        handleReload()
        lifecycleLoadOrder.forEach{it.listener(LifecycleEvent.RELOAD)}
    }


    /**
     * **DO NOT OVERRIDE**
     */
    override suspend fun onDisableAsync() {
        handleDisable()
        lifecycleDisableOrder.forEach{it.listener(LifecycleEvent.DISABLE)}
        commandManager.unregisterCommands()
        PluginManager::remove
        CoroutineScheduler::cancelAllTasks
    }

    /**
     * This is called when the server picks up your Plugin and has begun loading it.
     */
    protected open suspend fun handleLoad() {}

    /**
     * This is called Once the Plugin is ready to accept and register
     * events, commands etc.
     */
    protected open suspend fun handleEnable() {}

    /**
     * This will be called after your Plugin is done loading
     * and RacciCore has finished its loading process for your Plugin.
     *
     */
    protected open suspend fun handleAfterLoad() {}

    /**
     * This will be called whenever [RacciPlugin.reload] is called.
     *
     */
    protected open suspend fun handleReload() {}

    /**
     * This is triggered when your Plugin is being disabled by the Server,
     * Please use this to clean up your Plugin to not leak resources.
     */
    protected open suspend fun handleDisable() {}

    /**
     * The returned list of [KotlinListener]'s will be registered,
     * and enabled during the enable process.
     */
    protected open suspend fun registerListeners() : List<KotlinListener> {return emptyList()}

    /**
     * The returned list of [BaseCommand]'s will be registered,
     * and enabled during the enable process.
     */
    protected open suspend fun registerCommands() : List<BaseCommand> {return emptyList()}

    /**
     * The returned life of [LifecycleListener]'s will be registered,
     * and enabled during the enable process.
     */
    protected open suspend fun registerLifecycles() : List<Pair<LifecycleListener<*>, Int>> {return emptyList()}

}