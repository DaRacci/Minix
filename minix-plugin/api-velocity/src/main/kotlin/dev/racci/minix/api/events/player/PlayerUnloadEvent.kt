package dev.racci.minix.api.events.player

/**
 * Called once it is safe to handleUnload a Player's data.
 *
 * ## This method is Fired Asynchronously
 *
 * @property minixPlayer the [MinixPlayer] platform wrapper.
 */
public actual class PlayerUnloadEvent : MinixPlayerEvent()