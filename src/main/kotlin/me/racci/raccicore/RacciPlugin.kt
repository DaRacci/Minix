@file:Suppress("unused", "UNUSED_EXPRESSION")
package me.racci.raccicore

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import com.github.shynixn.mccoroutine.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.registerSuspendingEvents
import me.racci.raccicore.runnables.KotlinRunnable
import me.racci.raccicore.runnables.RunnableManager
import me.racci.raccicore.utils.UpdateChecker
import me.racci.raccicore.utils.extensions.KotlinListener
import me.racci.raccicore.utils.pm
import me.racci.raccicore.utils.strings.colour
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.ApiStatus

/**
 * Create the new plugin.
 * All values of the constructor are nullable
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

    lateinit var commandManager : PaperCommandManager ; private set
    val log = Log(colour("$colour$prefix"))

    override suspend fun onEnableAsync() {
        Log().info()
        Log().info("Loading ${this.colour} ${this.name}")
        Log().info()

        commandManager = PaperCommandManager(this)
        RacciPluginHandler::add

        if(spigotId != 0) ::UpdateChecker
        if(bStatsId != 0) {}//TODO

        Log().debug("HandleEnable Started")
        this.handleEnable()

        Log().debug("Registering Events")
        this.registerListeners().forEach {pm.registerSuspendingEvents(it, this)}
        Log().debug("Registering Runnables")
        RunnableManager.registeredRunnables[this] = this.registerRunnables()
        Log().debug("Starting Runnables")
        RunnableManager.run(this)
        Log().debug("Registering Commands")
        this.registerCommands().forEach(commandManager::registerCommand)

        Log().info()
        Log().success("Finished Loading ${this.colour} ${this.name}")
        Log().info()

        Log().debug("HandleAfterLoad Started")
        this.handleAfterLoad()

    }

    override suspend fun onDisableAsync() {
        this.commandManager.unregisterCommands()
        RacciPluginHandler::remove
        RunnableManager::close
        this.handleDisable()
    }

    override suspend fun onLoadAsync() {
        this.handleLoad()
    }

    suspend fun reload() {
        this.handleReload()
    }

    @Deprecated("", ReplaceWith("registerRunnables"))
    @ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
    protected fun registerRunnables(
        vararg runnables: BukkitRunnable,
        async: Boolean = false,
        repeating: Boolean = false,
        delay: Long = 5L,
        period: Long = 20L) {
        when (repeating) {
            true -> for (runnable in runnables) {
                if (async) {
                    runnable.runTaskTimerAsynchronously(this, delay, period)
                } else runnable.runTaskTimer(this, delay, period)
            }
            false -> for (runnable in runnables) {
                if (async) {
                    runnable.runTaskAsynchronously(this)
                } else runnable.runTask(this)
            }
        }
    }

    protected open suspend fun handleEnable() {}

    protected open suspend fun handleDisable() {}

    protected open suspend fun handleLoad() {}

    protected open suspend fun handleUnload() {}

    protected open suspend fun handleReload() {}

    protected open suspend fun handleAfterLoad() {}

    //protected open suspend fun registerHookLoaders() : List<AbstractHookService<*>> {return emptyList()}

    protected open suspend fun registerListeners() : List<KotlinListener> {return emptyList()}

    protected open suspend fun registerRunnables() : List<KotlinRunnable> {return emptyList()}

    protected open suspend fun registerCommands() : List<BaseCommand> {return emptyList()}

}