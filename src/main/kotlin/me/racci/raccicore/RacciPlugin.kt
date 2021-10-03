package me.racci.raccicore

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import me.racci.raccicore.listeners.KotlinListener
import me.racci.raccicore.runnables.KotlinRunnable
import me.racci.raccicore.utils.UpdateChecker
import me.racci.raccicore.utils.pm
import me.racci.raccicore.utils.strings.colour
import org.bukkit.Bukkit
import org.bukkit.event.Listener
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
abstract class RacciPlugin(colour: String = "",
                       prefix: String = "",
                       spigotId: Int = 0,
                       bStatsId: Int = 0) : org.bukkit.plugin.java.JavaPlugin() {

    protected var colour: String ; private set
    protected var prefix: String ; private set
    protected var spigotId: Int ; private set
    protected var bStatsId: Int ; private set
    protected lateinit var commandManager: PaperCommandManager ; private set
    protected var loadedHooks = HashSet<String>() ; private set
//    protected var lang: Lang? = null ; private set
//    protected lateinit var config: Config ; private set
//    protected var logger: Log; private set
    protected var outDated = false ; private set

    init {
        this.colour = colour(colour)
        this.prefix = colour(prefix)
        this.spigotId = spigotId
        this.bStatsId = bStatsId
    }

    protected fun registerListeners(vararg listeners: Listener) {
        listeners.onEach{pm.registerEvents(it, this)}
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

    override fun onEnable() {
        super.onEnable()

        this.commandManager = PaperCommandManager(this)

        this.logger.info("")
        this.logger.info("Loading ${this.colour} ${this.name}")

        if(this.spigotId != 0) {UpdateChecker(this, this.spigotId)}
        if(this.bStatsId != 0) {}//Register

        this.registerListeners().forEach {pm.registerEvents(it, this)}
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

    override fun onDisable() {
        super.onDisable()
        this.handleDisable()
    }

    override fun onLoad() {
        super.onLoad()
        this.handleLoad()
    }

    fun afterLoad() {
        this.handleAfterLoad()
        this.reload()
    }

    fun reload() {
        this.handleReload()
    }

    protected open fun handleEnable() {}

    protected open fun handleDisable() {}

    protected open fun handleLoad() {}

    protected open fun handleReload() {}

    protected open fun handleAfterLoad() {}

    protected open fun registerHookLoaders() {}

    protected open fun registerListeners() : List<KotlinListener> {return emptyList()}

    protected open fun registerRunnables() : List<BukkitTask> {return emptyList()}

    protected open fun registerCommands() : List<BaseCommand> {return emptyList()}


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