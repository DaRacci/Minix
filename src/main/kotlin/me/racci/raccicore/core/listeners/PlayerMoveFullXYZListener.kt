package me.racci.raccicore.core.listeners

import me.racci.raccicore.events.PlayerEnterLiquidEvent
import me.racci.raccicore.events.PlayerExitLiquidEvent
import me.racci.raccicore.events.PlayerMoveFullXYZEvent
import me.racci.raccicore.extensions.KotlinListener
import me.racci.raccicore.extensions.pm
import org.bukkit.Material
import org.bukkit.block.data.Waterlogged
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

class PlayerMoveFullXYZListener : KotlinListener {

    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerMoveFullXYZEvent(event: PlayerMoveFullXYZEvent) {
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
        } else null ?: return)
    }
}