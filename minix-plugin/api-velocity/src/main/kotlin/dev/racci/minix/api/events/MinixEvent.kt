package dev.racci.minix.api.events

/**
 * Represents a platform independent event.
 * Check your platform's event for more information.
 */
public actual abstract class MinixEvent {
    /** If this event is asynchronously fired. */
    actual val async: Boolean
        get() = TODO("Not yet implemented")

    /** If this event is cancelled. */
    actual var cancelled: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}

    actual fun toString(): String {
        TODO("Not yet implemented")
    }

    actual fun equals(other: Any?): Boolean {
        TODO("Not yet implemented")
    }

    actual fun hashCode(): Int {
        TODO("Not yet implemented")
    }

}