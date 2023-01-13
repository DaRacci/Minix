package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.events.CompanionEventHandler
import org.bukkit.Location
import org.bukkit.event.HandlerList

/**
 * This event is fired when the player moves one full block, So if the player moves +1 in any direction this will apply.
 *
 * ## This method is Fired Asynchronously
 *
 * @param player The player of this event.
 * @property from The previous location the player was at.
 * @property to The players new location.
 */
public class PlayerMoveFullXYZEvent(
    player: MinixPlayer,
    public val from: Location,
    public var to: Location
) : MinixPlayerEvent(player, true) {
    public operator fun component2(): Location = from
    public operator fun component3(): Location = to

    public companion object : CompanionEventHandler() {
        @JvmStatic override fun getHandlerList(): HandlerList = super.getHandlerList()
    }
}
