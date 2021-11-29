package me.racci.raccicore.api.events

import org.bukkit.World

/**
 * This event is fired when the world turns to Night.
 *
 * ## This method is Fired Asynchronously
 *
 * @param world The world of the event.
 */
class NightEvent(
    world: World
) : KWorldEvent(world, true)


/**
 * This event is fired when the world turns to Day.
 *
 * ## This method is Fired Asynchronously
 *
 * @param world The world of the event.
 */
class DayEvent(
    world: World
): KWorldEvent(world, true)