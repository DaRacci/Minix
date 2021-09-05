package me.racci.raccilib

import me.racci.raccilib.listeners.PlayerMoveFullXYZListener
import me.racci.raccilib.listeners.PlayerMoveListener
import me.racci.raccilib.listeners.PlayerTeleportListener
import me.racci.raccilib.utils.items.builders.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

class RacciLib : JavaPlugin() {

    override fun onEnable() {
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