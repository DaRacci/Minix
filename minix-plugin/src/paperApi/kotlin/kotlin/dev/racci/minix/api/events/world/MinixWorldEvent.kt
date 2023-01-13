package dev.racci.minix.api.events.world

import dev.racci.minix.api.events.MinixEvent.Companion.handlerMap
import org.bukkit.World
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.world.WorldEvent

/**
 * Represents a World event,
 * ## Includes Pre-done handlers and cancellable params.
 *
 * @param world The world, which this event happened in.
 */
public abstract class MinixWorldEvent(
    world: World
) : WorldEvent(world), Cancellable {

    private var cancelled: Boolean = false

    final override fun isCancelled(): Boolean = cancelled

    final override fun setCancelled(cancel: Boolean) { cancelled = cancel }

    final override fun getHandlers(): HandlerList = handlerMap[this::class]
}
