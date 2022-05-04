package dev.racci.minix.api.events

import org.bukkit.World
import org.bukkit.event.HandlerList

/**
 * This event is fired when the world turns to Night.
 *
 * @param world The world of the event.
 */
class WorldNightEvent(
    world: World
) : KWorldEvent(world) {
    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[WorldNightEvent::class]
    }
}

/**
 * This event is fired when the world turns to Day.
 *
 * @param world The world of the event.
 */
class WorldDayEvent(
    world: World
) : KWorldEvent(world) {
    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[WorldDayEvent::class]
    }
}
