package me.racci.raccicore.api.events

import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * This event is fired only when the player moves, this means that unlike
 * the normal PlayerMoveEvent it does not fire when the player looks around.
 *
 * ## This method is Fired Asynchronously
 *
 * @property from The previous location the player was at.
 * @property to The players new location.
 */
class PlayerMoveXYZEvent(
    player: Player,
    val from: Location,
    var to: Location
): KPlayerEvent(player, true)

/**
 * This event is fired when the player moves one full block,
 * So if the player moves +1 in any direction this will apply.
 *
 * ## This method is Fired Asynchronously
 *
 * @property from The previous location the player was at.
 * @property to The players new location.
 */
class PlayerMoveFullXYZEvent(
    player: Player,
    val from: Location,
    var to: Location
): KPlayerEvent(player, true)