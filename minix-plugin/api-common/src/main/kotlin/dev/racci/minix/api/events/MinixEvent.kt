package dev.racci.minix.api.events

/**
 * Represents a platform independent event.
 * Check your platform's event for more information.
 */
public expect abstract class MinixEvent {

    /** If this event is asynchronously fired. */
    public val async: Boolean

    override fun toString(): String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}
