package dev.racci.minix.api.events

import org.bukkit.event.Cancellable

public actual interface MinixCancellable : Cancellable {
    /** If this event is cancelled. */
    public actual var actualCancelled: Boolean

    override fun isCancelled(): Boolean = actualCancelled

    override fun setCancelled(cancel: Boolean) { actualCancelled = cancel }
}
