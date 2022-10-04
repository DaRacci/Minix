package dev.racci.minix.api.events.world

import dev.racci.minix.api.events.KEvent
import org.bukkit.World
import org.bukkit.event.HandlerList

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
