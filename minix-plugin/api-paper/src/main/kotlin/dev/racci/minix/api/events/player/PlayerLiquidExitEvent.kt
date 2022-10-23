package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.enums.LiquidType
import dev.racci.minix.api.events.CompanionEventHandler
import dev.racci.minix.api.data.MinixPlayer
import org.bukkit.event.HandlerList

public class PlayerLiquidExitEvent(
    player: MinixPlayer,
    previousType: LiquidType,
    newType: LiquidType
) : PlayerLiquidEvent(player, previousType, newType) {

    public companion object : CompanionEventHandler() {
        @JvmStatic
        override fun getHandlerList(): HandlerList = super.getHandlerList()
    }
}
