@file:Suppress("unused", "UNUSED_EXPRESSION")
package me.racci.raccicore

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import com.github.shynixn.mccoroutine.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.registerSuspendingEvents
import me.racci.raccicore.runnables.Scheduler
import me.racci.raccicore.skedule.bukkitScheduler
import me.racci.raccicore.utils.UpdateChecker
import me.racci.raccicore.utils.extensions.KotlinListener
import me.racci.raccicore.utils.pm
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.*
import java.util.stream.Collectors

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

    lateinit var commandManager  : PaperCommandManager      ; private set
    lateinit var scheduler       : Scheduler                ; private set
    var loadedHooks     : ArrayList<String>     = ArrayList()   ; private set
    var outDated        : Boolean               = false         ; private set



    override suspend fun onEnableAsync() {
        this.commandManager = PaperCommandManager(this)

        info("")
        info("Loading ${this.colour} ${this.name}")

        if(this.spigotId != 0) ::UpdateChecker
        if(this.bStatsId != 0) {}//Register

        this.registerListeners().forEach {pm.registerSuspendingEvents(it, this)}
        this.registerRunnables().forEach {}
        this.registerCommands().forEach(commandManager::registerCommand)


        val enabledPlugins: Set<String> = Arrays.stream(pm.plugins)
            .map(Plugin::getName)
            .map(String::lowercase)
            .collect(Collectors.toSet())

        if(enabledPlugins.contains("PlaceholderAPI".lowercase())) {
            this.loadedHooks.add("PlaceHolderAPI")
        }

        this.handleEnable()

        this.logger.info("")

    }

    override suspend fun onDisableAsync() {
        this.commandManager.unregisterCommands()
        this.loadedHooks.clear()
        bukkitScheduler.cancelTasks(this)
        this.handleDisable()
    }

    override suspend fun onLoadAsync() {
        this.handleLoad()
        this.handleAfterLoad()
    }

    suspend fun reload() {
        this.handleReload()
    }

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

    protected open suspend fun handleReload() {}

    protected open suspend fun handleAfterLoad() {}

    protected open suspend fun registerHookLoaders() {}

    protected open suspend fun registerListeners() : List<KotlinListener> {return emptyList()}

    protected open suspend fun registerRunnables() : List<BukkitTask> {return emptyList()}

    protected open suspend fun registerCommands() : List<BaseCommand> {return emptyList()}


//    protected class LogBuilder {
//        private var fvar1: String =
//        private var fvar2: String = "&b"
//        private var fvar3: String = "&4[&c&lERROR&4]"
//        private var fvar4: String = "&e[&6&lWARNING&e]"
//        private var fvar5: String = "&8[&e&lINFO&8]"
//        private var fvar6: String = "&2[&a&lSUCCESS&2]"
//        fun prefix(prefix: String) {
//            fvar1 = colour(prefix)
//        }
//        fun textColour(colour: String) {
//            fvar2 = colour(colour)
//        }
//        fun error(prefix: String) {
//            fvar3 = colour(prefix)
//        }
//        fun warning(prefix: String) {
//            fvar4 = colour(prefix)
//        }
//        fun info(prefix: String) {
//            fvar5 = colour(prefix)
//        }
//        fun success(prefix: String) {
//            fvar6 = colour(prefix)
//        }
//        fun build() : Log {
//            return Log(fvar1, fvar2, fvar3, fvar4, fvar5, fvar6)
//        }
//    }
//
//    protected data class Log(
//        val prefix: String,
//        val colour: String,
//        val error: String,
//        val warning: String,
//        val info: String,
//        val success: String,
//    )
//
//    protected fun logBuilder(init: LogBuilder.() -> Unit): Log {
//        val log = LogBuilder()
//        log.init()
//        return log.build()
//    }
}