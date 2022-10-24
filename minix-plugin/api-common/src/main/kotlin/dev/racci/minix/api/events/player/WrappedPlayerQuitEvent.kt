package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.MinixPlayer

public expect class WrappedPlayerQuitEvent {
    public val player: MinixPlayer

    public val wrappedEvent: Any
}
