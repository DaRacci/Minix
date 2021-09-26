package me.racci.raccilib

import me.racci.raccilib.data.PlayerManager
import me.racci.raccilib.listeners.*
import org.bukkit.plugin.PluginManager

lateinit var racciCore : RacciLib ; private set
lateinit var playerManager : PlayerManager ; private set

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

    override fun onEnable() {
        racciCore = this
        instance = this
        playerManager = PlayerManager()
        registerListeners()
        registerCommands()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    fun handleReload() {
        // Plugin reload logic
    }

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