package dev.racci.minix.api.events.world

import dev.racci.minix.api.events.CompanionEventHandler
import org.bukkit.World
import org.bukkit.event.HandlerList

/**
 * This event is fired when the world turns to Night.
 *
 * @param world The world of the event.
 */
public class WorldNightEvent(
    world: World
) : MinixWorldEvent(world) {
    public companion object : CompanionEventHandler() {
        @JvmStatic override fun getHandlerList(): HandlerList = super.getHandlerList()
    }
}
