@file:JvmName("RacciLib")
package me.racci.raccilib

import me.racci.raccilib.listeners.PlayerMoveFullXYZListener
import me.racci.raccilib.listeners.PlayerMoveListener
import me.racci.raccilib.listeners.PlayerTeleportListener
import org.bukkit.plugin.PluginManager

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
        instance = this
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
    }

}