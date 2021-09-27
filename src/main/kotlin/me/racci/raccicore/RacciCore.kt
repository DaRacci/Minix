package me.racci.raccicore

import me.racci.raccicore.data.PlayerManager
import me.racci.raccicore.listeners.*
import org.bukkit.plugin.PluginManager

/**
 * Racci core
 */
lateinit var racciCore : RacciLib ; private set

/**
 * Player manager
 */
lateinit var playerManager : PlayerManager ; private set

/**
 * Racci lib
 *
 * @constructor Create empty Racci lib
 */
class RacciLib : RacciPlugin(
    "&8",
    "RacciLib",
    null,
    null
) {

    companion object {
        var instance: RacciLib? = null
            private set
    }

    /**
     * On enable
     *
     */
    override fun onEnable() {
        racciCore = this
        instance = this
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

}