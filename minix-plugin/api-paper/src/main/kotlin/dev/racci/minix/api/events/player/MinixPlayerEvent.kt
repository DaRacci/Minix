package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.events.MinixEvent
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

/**
 * Represents a player event.
 *
 * ## Includes Pre done handler,
 * however you still need to add your own static handler.
 * @see PlayerMoveXYZEvent
 *
 * @param minixPlayer The player of the event.
 * @param async If the event is Asynchronous.
 */
public actual abstract class MinixPlayerEvent(
    public actual val minixPlayer: MinixPlayer,
    public actual val async: Boolean = false
) : PlayerEvent(minixPlayer.actualPlayer.player ?: error("Player event cannot be called without an online player!"), async) {
    final override fun getHandlers(): HandlerList = MinixEvent.handlerMap[this::class]
}
