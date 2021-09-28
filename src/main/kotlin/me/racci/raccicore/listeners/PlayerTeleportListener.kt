package me.racci.raccicore.listeners

import me.racci.raccicore.RacciCore
import me.racci.raccicore.events.PlayerMoveFullXYZEvent
import me.racci.raccicore.events.PlayerMoveXYZEvent
import me.racci.raccicore.skedule.SynchronizationContext
import me.racci.raccicore.skedule.schedule
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.scheduler.BukkitScheduler

/**
 * Player teleport listener
 *
 * @property plugin
 * @constructor Create empty Player teleport listener
 */
class PlayerTeleportListener(
    private val plugin: RacciCore
) : Listener {

    private val scheduler: BukkitScheduler = Bukkit.getScheduler()

    /**
     * On player teleport
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        if(event.isCancelled) return

        scheduler.schedule(plugin, SynchronizationContext.ASYNC) {

            // PlayerMoveFullXYZEvent
            var playerMoveFullXYZEvent: PlayerMoveFullXYZEvent? = null
            if (event.from.blockX != event.to.blockX || event.from.blockY != event.to.blockY || event.from.blockZ != event.to.blockZ) {
                playerMoveFullXYZEvent = PlayerMoveFullXYZEvent(event.player, event.from, event.to)

                playerMoveFullXYZEvent.isCancelled = event.isCancelled
            }
            // PlayerMoveXYZEvent
            var playerMoveXYZEvent: PlayerMoveXYZEvent? = null
            if (event.from.x != event.to.x || event.from.y != event.to.y || event.from.z != event.to.z) {
                playerMoveXYZEvent = PlayerMoveXYZEvent(event.player, event.from, event.to)

                playerMoveXYZEvent.isCancelled = event.isCancelled
            }
            // Call the new events
            for (newEvent in listOf(playerMoveXYZEvent, playerMoveFullXYZEvent)) {
                if (newEvent != null) {
                    Bukkit.getPluginManager().callEvent(newEvent)
                }
            }
            // Check for cancelling events
            switchContext(SynchronizationContext.ASYNC)
            var isCancelled = event.isCancelled
            if (playerMoveFullXYZEvent != null && playerMoveFullXYZEvent.isCancelled) {
                isCancelled = true
                playerMoveXYZEvent?.isCancelled = true
            }
            if (playerMoveXYZEvent != null && playerMoveXYZEvent.isCancelled) {
                isCancelled = true
                playerMoveFullXYZEvent?.isCancelled = true
            }
            event.isCancelled = isCancelled
        }
    }

}