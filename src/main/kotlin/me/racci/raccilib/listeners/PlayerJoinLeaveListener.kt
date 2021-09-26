package me.racci.raccilib.listeners

import me.racci.raccilib.data.PlayerData
import me.racci.raccilib.playerManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerJoinLeaveListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        playerManager.addPlayerData(PlayerData(event.player))
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        playerManager.removePlayerData(event.player.uniqueId)
    }

}