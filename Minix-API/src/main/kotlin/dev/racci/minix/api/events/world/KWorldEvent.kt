package dev.racci.minix.api.events.world

import dev.racci.minix.api.events.KEvent.Companion.handlerMap
import org.bukkit.World
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.world.WorldEvent

/**
 * Represents a World event,
 * Includes variables [isOverworld] [isNether] and [isEnd]
 * These make it easy to find what world type the event is within.
 *
 * ## Includes Pre-done handlers and cancellable params
 *
 * @param world The world, which this event happened in.
 */
abstract class KWorldEvent(
    world: World
) : WorldEvent(world), Cancellable {

    val isOverworld get() = world.environment == World.Environment.NORMAL
    val isNether get() = world.environment == World.Environment.NETHER
    val isEnd get() = world.environment == World.Environment.THE_END
    val isCustom get() = world.environment == World.Environment.CUSTOM

    private var cancelled: Boolean = false

    override fun isCancelled() = cancelled

    override fun setCancelled(cancel: Boolean) { cancelled = cancel }

    override fun getHandlers(): HandlerList = handlerMap[this::class]
}
