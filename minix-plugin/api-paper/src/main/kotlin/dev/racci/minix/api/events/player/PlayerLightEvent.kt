package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.events.CompanionEventHandler
import org.bukkit.event.HandlerList

/**
 * Called when a player's light-level changes, be this due to moving or due to a block change.
 *
 * @param player The player whose light-level changed
 * @param oldLightLevel The player's previous light-level
 * @property newLightLevel The player's new light-level
 * @property cause The cause of the light-level change
 */
public class PlayerLightEvent(
    player: MinixPlayer,
    public val oldLightLevel: Int,
    public val newLightLevel: Int,
    public val cause: Cause
) : MinixPlayerEvent(player, true) {

    public enum class Cause {
        BLOCK_UPDATE,
        MOVEMENT;
    }

    public companion object : CompanionEventHandler() {
        @JvmStatic override fun getHandlerList(): HandlerList = super.getHandlerList()
    }
}
