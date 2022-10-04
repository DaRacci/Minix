package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.enums.LiquidType
import dev.racci.minix.api.events.CompanionEventHandler
import org.bukkit.entity.Player

class PlayerLiquidExitEvent(
    player: Player,
    previousType: LiquidType,
    newType: LiquidType
) : PlayerLiquidEvent(player, previousType, newType) {

    companion object : CompanionEventHandler() {
        @JvmStatic
        override fun getHandlerList() = super.getHandlerList()
    }
}
