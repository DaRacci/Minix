package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.data.enums.LiquidType

public sealed class PlayerLiquidEvent(
    player: MinixPlayer,
    public val previousType: LiquidType,
    public val newType: LiquidType
) : MinixPlayerEvent(player, true) {

    public operator fun component2(): LiquidType = previousType
    public operator fun component3(): LiquidType = newType
}
