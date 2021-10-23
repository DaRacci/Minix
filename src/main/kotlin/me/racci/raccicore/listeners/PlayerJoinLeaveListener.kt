package me.racci.raccicore.listeners

import me.racci.raccicore.data.PlayerData
import me.racci.raccicore.data.PlayerManager
import me.racci.raccicore.utils.extensions.KotlinListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * Player join leave listener
 *
 * @constructor Create empty Player join leave listener
 */
class PlayerJoinLeaveListener : KotlinListener {

    /**
     * On join
     *
     * @param event
     */
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        PlayerData(event.player)
    }

    /**
     * On leave
     *
     * @param event
     */
    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        PlayerManager.removePlayerData(event.player.uniqueId)
    }

}