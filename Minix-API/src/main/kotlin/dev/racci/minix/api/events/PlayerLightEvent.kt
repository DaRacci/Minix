package dev.racci.minix.api.events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

class PlayerLightEvent(
    player: Player,
    val lightLevel: Int,
) : KPlayerEvent(player, true) {
    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerShiftLeftClickEvent::class]
    }
}
