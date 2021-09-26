package me.racci.raccilib.listeners

import me.racci.raccilib.data.PlayerData
import me.racci.raccilib.playerManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * Player join leave listener
 *
 * @constructor Create empty Player join leave listener
 */
class PlayerJoinLeaveListener : Listener {

    /**
     * On join
     *
     * @param event
     */
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        playerManager.addPlayerData(PlayerData(event.player))
    }

    /**
     * On leave
     *
     * @param event
     */
    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        playerManager.removePlayerData(event.player.uniqueId)
    }

}