package dev.racci.minix.api.events

import org.bukkit.entity.Player

class PlayerLightEvent(
    player: Player,
    val lightLevel: Int,
) : KPlayerEvent(player, true)
