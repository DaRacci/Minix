package dev.racci.minix.api.events.player

import dev.racci.minix.api.events.KEvent.Companion.handlerMap
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

/**
 * Represents a player event.
 *
 * ## Includes Pre done handlers and cancellable params,
 * however you still need to add your own static handler
 * @see PlayerMoveXYZEvent
 *
 * @param player The player of the event.
 * @param async If the event is Asynchronous.
 */
abstract class KPlayerEvent(
    player: Player,
    async: Boolean = false
) : PlayerEvent(player, async), Cancellable {

    private var cancelled: Boolean = false

    override fun isCancelled() = cancelled

    override fun setCancelled(cancel: Boolean) { cancelled = cancel }

    override fun getHandlers(): HandlerList = handlerMap[this::class]
}
