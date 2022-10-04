package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.enums.LiquidType
import org.bukkit.entity.Player

sealed class PlayerLiquidEvent(
    player: Player,
    val previousType: LiquidType,
    val newType: LiquidType
) : KPlayerEvent(player, true) {

    operator fun component2(): LiquidType = previousType
    operator fun component3(): LiquidType = newType
}
