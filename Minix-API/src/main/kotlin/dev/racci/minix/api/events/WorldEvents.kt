@file:Suppress("UNUSED")

package dev.racci.minix.api.events

import org.bukkit.World

/**
 * This event is fired when the world turns to Night.
 *
 * @param world The world of the event.
 */
class WorldNightEvent(
    world: World
) : KWorldEvent(world)

/**
 * This event is fired when the world turns to Day.
 *
 * @param world The world of the event.
 */
class WorldDayEvent(
    world: World
) : KWorldEvent(world)
