package me.racci.raccicore.listeners

import me.racci.raccicore.events.PlayerEnterLiquidEvent
import me.racci.raccicore.events.PlayerExitLiquidEvent
import me.racci.raccicore.events.PlayerMoveFullXYZEvent
import me.racci.raccicore.racciCore
import me.racci.raccicore.skedule.skeduleAsync
import me.racci.raccicore.utils.blocks.isLiquid
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

/**
 * Player move full x y z listener
 *
 * @property plugin
 * @constructor Create empty Player move full x y z listener
 */
class PlayerMoveFullXYZListener : KotlinListener {

    /**
     * On player move full x y z event
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerMoveFullXYZEvent(event: PlayerMoveFullXYZEvent) {
        skeduleAsync(racciCore) {
            val from: Block = event.from.block
            val to: Block = event.to.block
            val var1: Int = isLiquid(from)
            val var2: Int = isLiquid(to)
            var newEvent: Event? = null
            if (var1 == 0) {
                newEvent = when (var2) {
                    1, 2 -> PlayerEnterLiquidEvent(event.player, var2, from, to)
                    else -> null
                }
            } else if (var2 == 0) {
                newEvent = when (var1) {
                    1, 2 -> PlayerExitLiquidEvent(event.player, var1, from, to)
                    else -> null
                }
            }
            if (newEvent != null) {
                Bukkit.getPluginManager().callEvent(newEvent)
            }
        }
    }
}