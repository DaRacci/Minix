@file:Suppress("unused", "UNUSED_EXPRESSION")
package me.racci.raccicore

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import com.github.shynixn.mccoroutine.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.registerSuspendingEvents
import me.racci.raccicore.scheduler.CoroutineScheduler
import me.racci.raccicore.utils.UpdateChecker
import me.racci.raccicore.utils.extensions.KotlinListener
import me.racci.raccicore.utils.extensions.pm
import me.racci.raccicore.utils.strings.colour
import kotlin.properties.Delegates

/**
 * Create the new plugin.
 * All values of the constructor have defaults and are not required.
 *
 * @param colour        The colour for console messages
 * @param prefix        The prefix for console messages
 * @param spigotId      The spigot ID for the plugin
 * @param bStatsId      The bStats ID for the plugin
 */
abstract class RacciPlugin(
    val colour  : String    = "",
    val prefix  : String    = "",
    val spigotId: Int       = 0,
    val bStatsId: Int       = 0
) : SuspendingJavaPlugin() {

    var commandManager by Delegates.notNull<PaperCommandManager>() ; private set
    val log = Log(colour("$colour$prefix"))

    override suspend fun onEnableAsync() {
        log.info("")
        log.info("Loading ${this.colour} ${this.name}")
        log.info("")

        commandManager = PaperCommandManager(this)
        PluginManager::add

        if(spigotId != 0) ::UpdateChecker
        //if(bStatsId != 0) {}//TODO

        log.debug("HandleEnable Started")
        this.handleEnable()

        log.debug("Registering Events")
        this.registerListeners().forEach {pm.registerSuspendingEvents(it, this)}
        log.debug("Registering Commands")
        this.registerCommands().forEach(commandManager::registerCommand)

        log.info("")
        log.success("Finished Loading ${this.colour} ${this.name}")
        log.info("")

        log.debug("HandleAfterLoad Started")
        this.handleAfterLoad()

    }

    override suspend fun onDisableAsync() {
        this.commandManager.unregisterCommands()
        PluginManager::remove
        CoroutineScheduler::cancelAllTasks
        this.handleDisable()
    }

    override suspend fun onLoadAsync() {
        this.handleLoad()
    }

    suspend fun reload() {
        this.handleReload()
    }

    protected open suspend fun handleEnable() {}

    protected open suspend fun handleDisable() {}

    protected open suspend fun handleLoad() {}

    protected open suspend fun handleUnload() {}

    protected open suspend fun handleReload() {}

    protected open suspend fun handleAfterLoad() {}

    //protected open suspend fun registerHookLoaders() : List<AbstractHookService<*>> {return emptyList()}

    protected open suspend fun registerListeners() : List<KotlinListener> {return emptyList()}

    protected open suspend fun registerCommands() : List<BaseCommand> {return emptyList()}

}