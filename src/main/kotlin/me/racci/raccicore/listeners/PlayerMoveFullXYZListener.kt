package me.racci.raccicore.listeners

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.racci.raccicore.events.PlayerEnterLiquidEvent
import me.racci.raccicore.events.PlayerExitLiquidEvent
import me.racci.raccicore.events.PlayerMoveFullXYZEvent
import me.racci.raccicore.utils.extensions.KotlinListener
import me.racci.raccicore.utils.pm
import org.bukkit.Material
import org.bukkit.block.data.Waterlogged
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
    suspend fun onPlayerMoveFullXYZEvent(event: PlayerMoveFullXYZEvent) = withContext(Dispatchers.IO) {
        val array = object : ArrayList<Int>() {init {
            for(block in listOf(event.from.block, event.to.block)) {
                add(when(block.type) {
                    Material.WATER -> 1
                    Material.LAVA -> 2
                    else -> if((block.blockData is Waterlogged) && (block.blockData as Waterlogged).isWaterlogged) 1 else 0
                })
            }
        }}
        pm.callEvent(if(array[0] == 0) {
            PlayerEnterLiquidEvent(event.player, array[1], event.from, event.to)
        } else if(array[1] == 0) {
            PlayerExitLiquidEvent(event.player, array[0], event.from, event.to)
        } else null ?: return@withContext)
    }
}