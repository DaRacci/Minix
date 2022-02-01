@file:Suppress("UNUSED")

package dev.racci.minix.api.events

import org.bukkit.World
import org.jetbrains.annotations.ApiStatus

/**
 * This event is fired when the world turns to Night.
 *
 * @param world The world of the event.
 */
@ApiStatus.AvailableSince("0.3.0")
class WorldNightEvent(
    world: World
) : KWorldEvent(world)

/**
 * This event is fired when the world turns to Day.
 *
 * @param world The world of the event.
 */
@ApiStatus.AvailableSince("0.3.0")
class WorldDayEvent(
    world: World
) : KWorldEvent(world)
