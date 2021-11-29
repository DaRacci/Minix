package me.racci.raccicore.core.listeners

import me.racci.raccicore.api.events.PlayerMoveFullXYZEvent
import me.racci.raccicore.api.events.PlayerMoveXYZEvent
import me.racci.raccicore.api.extensions.KotlinListener
import me.racci.raccicore.api.extensions.pm
import org.bukkit.event.EventHandler

class PlayerMoveXYZListener: KotlinListener {

    @EventHandler
    fun onMove(event: PlayerMoveXYZEvent) {
        if(event.from.blockX != event.to.blockX
           || event.from.blockY != event.to.blockY
           || event.from.blockZ != event.to.blockZ) {
            pm.callEvent(PlayerMoveFullXYZEvent(event.player, event.from, event.to))
        }
    }

}