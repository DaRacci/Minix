package dev.racci.minix.api.events.player

import dev.racci.minix.api.events.CompanionEventHandler
import org.bukkit.entity.Player

/**
 * Called when a player's light-level changes, be this due to moving or due to a block change.
 *
 * @param player The player whose light-level changed
 * @param oldLightLevel The player's previous light-level
 * @property newLightLevel The player's new light-level
 * @property cause The cause of the light-level change
 */
class PlayerLightEvent(
    player: Player,
    val oldLightLevel: Int,
    val newLightLevel: Int,
    val cause: Cause
) : KPlayerEvent(player, true) {

    enum class Cause {
        BLOCK_UPDATE,
        MOVEMENT;
    }

    companion object : CompanionEventHandler() {
        @JvmStatic override fun getHandlerList() = super.getHandlerList()
    }
}
