package dev.racci.minix.api.events.world

import dev.racci.minix.api.events.MinixEvent
import org.bukkit.World
import org.bukkit.event.HandlerList

/**
 * This event is fired when the world turns to Day.
 *
 * @param world The world of the event.
 */
public class WorldDayEvent(
    world: World
) : MinixWorldEvent(world) {
    public companion object {
        @JvmStatic
        public fun getHandlerList(): HandlerList = MinixEvent.handlerMap[WorldDayEvent::class]
    }
}
