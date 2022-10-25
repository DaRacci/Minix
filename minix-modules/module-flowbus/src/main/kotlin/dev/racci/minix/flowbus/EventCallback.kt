package dev.racci.minix.flowbus

/**
 * Represent an event callback.
 *
 * @param T the event type, if this event is generic / not final,
 * all posted events that inherit from this event will be received.
 */
public fun interface EventCallback<T> {
    public val priority: Priority get() = Priority.DEFAULT

    public fun onEvent(event: T)

    public companion object {
        public inline fun <reified T> of(
            priority: Priority = Priority.DEFAULT,
            crossinline callback: (T) -> Unit
        ): EventCallback<T> = object : EventCallback<T> {
            override val priority: Priority get() = priority
            override fun onEvent(event: T) = callback(event)
        }

        public fun of(priority: Priority): MutableEventCallbackBuilder = MutableEventCallbackBuilder().priority(priority)
    }
}
