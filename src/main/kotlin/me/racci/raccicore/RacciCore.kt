package me.racci.raccicore

import me.racci.raccicore.data.PlayerManager
import me.racci.raccicore.listeners.*
import me.racci.raccicore.runnables.TimeRunnable
import org.bukkit.plugin.PluginManager

/**
 * Racci core
 */
internal lateinit var racciCore : RacciCore ; private set

/**
 * Player manager
 */
lateinit var playerManager : PlayerManager ; private set

/**
 * Racci lib
 *
 * @constructor Create empty Racci lib
 */
class RacciCore : RacciPlugin(
    "&8",
    "RacciCore",
    null,
    null
) {

    /**
     * On enable
     *
     */
    override fun onEnable() {
        racciCore = this
        playerManager = PlayerManager()
        registerListeners()
        registerCommands()
    }

    /**
     * On disable
     *
     */
    override fun onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Handle reload
     *
     */
    fun handleReload() {
        // Plugin reload logic
    }

    /**
     * Handler after load
     *
     */
    fun handlerAfterLoad() {
        // Plugin after load logic
    }

    private fun registerCommands() {
        // Plugin command logic
    }

    private fun registerListeners() {
        val pm: PluginManager = server.pluginManager
        pm.registerEvents(PlayerMoveListener(this), this)
        pm.registerEvents(PlayerTeleportListener(this), this)
        pm.registerEvents(PlayerMoveFullXYZListener(this), this)
        pm.registerEvents(PlayerComboListeners(), this)
        pm.registerEvents(PlayerJoinLeaveListener(), this)
    }

    private fun registerRunnables() {
        val pm: PluginManager = server.pluginManager
        TimeRunnable(pm).runTaskTimerAsynchronously(this, 0L, 20)
    }

}