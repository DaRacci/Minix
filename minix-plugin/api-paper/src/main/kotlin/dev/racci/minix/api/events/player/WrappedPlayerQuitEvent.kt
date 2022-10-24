package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.MinixPlayer

/** Handles PlayerQuitEvent and PlayerKickEvent */
public actual class WrappedPlayerQuitEvent internal constructor(
    public actual val player: MinixPlayer,
    public actual val wrappedEvent: Any
)
