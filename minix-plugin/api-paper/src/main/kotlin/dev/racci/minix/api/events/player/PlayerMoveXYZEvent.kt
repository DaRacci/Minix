package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.events.CompanionEventHandler
import org.bukkit.Location
import org.bukkit.event.HandlerList

/**
 * This event is fired only when the player moves, this means that unlike the normal PlayerMoveEvent it does not fire when the player looks around.
 *
 * ## This method is Fired Asynchronously
 *
 * @param player The player of this event.
 * @property from The previous location the player was at.
 * @property to The players new location.
 */
public class PlayerMoveXYZEvent(
    player: MinixPlayer,
    public val from: Location,
    public var to: Location
) : MinixPlayerEvent(player, true) {

    public fun hasExplicitlyChangedBlock(): Boolean =
        from.blockX != to.blockX || from.blockY != to.blockY || from.blockZ != to.blockZ

    public operator fun component2(): Location = from
    public operator fun component3(): Location = to

    public companion object : CompanionEventHandler() {
        @JvmStatic override fun getHandlerList(): HandlerList = super.getHandlerList()
    }
}
