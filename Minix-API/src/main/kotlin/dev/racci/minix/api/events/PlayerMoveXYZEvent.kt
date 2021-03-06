package dev.racci.minix.api.events

import org.bukkit.Location
import org.bukkit.entity.Player
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
class PlayerMoveXYZEvent(
    player: Player,
    val from: Location,
    var to: Location
) : KPlayerEvent(player, true) {

    fun hasExplicitlyChangedBlock() =
        from.blockX != to.blockX || from.blockY != to.blockY || from.blockZ != to.blockZ

    operator fun component2(): Location = from
    operator fun component3(): Location = to

    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerMoveXYZEvent::class]
    }
}

/**
 * This event is fired when the player moves one full block, So if the player moves +1 in any direction this will apply.
 *
 * ## This method is Fired Asynchronously
 *
 * @param player The player of this event.
 * @property from The previous location the player was at.
 * @property to The players new location.
 */
class PlayerMoveFullXYZEvent(
    player: Player,
    val from: Location,
    var to: Location
) : KPlayerEvent(player, true) {

    operator fun component2(): Location = from
    operator fun component3(): Location = to

    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerMoveFullXYZEvent::class]
    }
}
