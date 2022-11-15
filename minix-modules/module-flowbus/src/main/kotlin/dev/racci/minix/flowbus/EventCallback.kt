package dev.racci.minix.flowbus

import dev.racci.minix.api.data.Priority
import dev.racci.minix.flowbus.receiver.EventReceiver

/**
 * Represent an event callback for an [EventReceiver].
 *
 * @param T the event type, if this event is generic / not final,
 * all posted events that inherit from this event will be received.
 * @param priority The priority of this listener in the events' collection. (Currently Non Functional)
 * @param ignoreCancelled Whether to ignore events that are cancelled. (Currently Non functional)
 * @param skipRetained Skips event already present in the flow. This is `false` by default.
 * @param callback The callback function.
 *
 * @see [EventReceiver.subscribe]
 * @see [EventReceiver.subscribeTo]
 */
public data class EventCallback<T : Any> @PublishedApi internal constructor(
    public val priority: Priority = Priority.DEFAULT,
    public val ignoreCancelled: Boolean = false,
    public val skipRetained: Boolean = false,
    public val callback: suspend (event: T) -> Unit
) {
    public companion object {
        public inline fun <reified T : Any> of(
            priority: Priority,
            ignoreCancelled: Boolean = false,
            skipRetained: Boolean = false,
            noinline callback: (event: T) -> Unit
        ): EventCallback<T> = EventCallback(
            priority = priority,
            ignoreCancelled = ignoreCancelled,
            skipRetained = skipRetained,
            callback = callback
        )

        public inline fun <reified T : Any> of(
            ignoreCancelled: Boolean,
            skipRetained: Boolean = false,
            noinline callback: suspend (event: T) -> Unit
        ): EventCallback<T> = EventCallback(
            ignoreCancelled = ignoreCancelled,
            skipRetained = skipRetained,
            callback = callback
        )

        public inline fun <reified T : Any> of(
            noinline callback: suspend T.() -> Unit
        ): EventCallback<T> = EventCallback(callback = callback)
    }
}
