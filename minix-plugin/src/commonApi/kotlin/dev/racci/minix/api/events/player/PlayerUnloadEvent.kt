package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.events.MinixEvent

/**
 * Called once it is safe to handleUnload a Player's data.
 *
 * ## This method is Fired Asynchronously
 *
 * @property minixPlayer the [MinixPlayer] platform wrapper.
 */
public expect class PlayerUnloadEvent(minixPlayer: MinixPlayer) : MinixEvent {
    public val minixPlayer: MinixPlayer
}
