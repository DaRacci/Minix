package me.racci.raccilib.listeners

import me.racci.raccilib.RacciLib
import me.racci.raccilib.events.PlayerMoveFullXYZEvent
import me.racci.raccilib.events.PlayerMoveXYZEvent
import me.racci.raccilib.skedule.skeduleAsync
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

/**
 * Player move listener
 *
 * @property plugin
 * @constructor Create empty Player move listener
 */
class PlayerMoveListener(private val plugin: RacciLib): Listener {

    private infix fun Location.equalsBlock(other: Location) =
        this.blockX == other.blockX && this.blockY == other.blockY && this.blockZ == other.blockZ
    private infix fun Location.equalsExact(other: Location) =
        this.x == other.x && this.y == other.y && this.z == other.z

    /**
     * On player move
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (event.isCancelled) return

        skeduleAsync(plugin) {
            var playerMoveFullXYZEvent: PlayerMoveFullXYZEvent? = null
            var playerMoveXYZEvent: PlayerMoveXYZEvent? = null
            var isCancelled = event.isCancelled

            if(!(event.from equalsBlock event.to)) {
                playerMoveFullXYZEvent = PlayerMoveFullXYZEvent(event.player, event.from, event.to)
                playerMoveFullXYZEvent.isCancelled = isCancelled
            }
            if(!(event.from equalsExact event.to)) {
                playerMoveXYZEvent = PlayerMoveXYZEvent(event.player, event.from, event.to)
                playerMoveXYZEvent.isCancelled = isCancelled
            }

            if(playerMoveFullXYZEvent != null) Bukkit.getPluginManager().callEvent(playerMoveFullXYZEvent)
            if(playerMoveXYZEvent != null) Bukkit.getPluginManager().callEvent(playerMoveXYZEvent)

            if (playerMoveFullXYZEvent?.isCancelled == true) {
                isCancelled = true
                playerMoveXYZEvent?.isCancelled = true
            }
            if (playerMoveXYZEvent?.isCancelled == true) {
                isCancelled = true
                playerMoveFullXYZEvent?.isCancelled = true
            }
            event.isCancelled = isCancelled
        }
    }

}
