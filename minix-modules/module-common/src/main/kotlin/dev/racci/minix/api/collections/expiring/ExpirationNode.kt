package dev.racci.minix.api.collections.expiring

import kotlinx.datetime.Instant
import org.jetbrains.annotations.ApiStatus
import kotlin.time.Duration

/**
 * A representation of an element in an [ExpirationCollection].
 * This element holds the element itself, the time it was added, and the time it will expire, and its callback.
 * This also acts as a node in a linked list.
 *
 * @param E The type of element in the collection.
 * @property value The element.
 * @property expireIn How long from the current time the element will expire.
 * @property startTime The time the element was created/added.
 * @property onExpire The [ExpirationCallback] which might be executed when the element expires.
 * @property next The next node in the linked list.
 * @property previous The previous node in the linked list.
 */
@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public data class ExpirationNode<E> internal constructor(
    override val value: E,
    override val expireIn: Duration,
    override val startTime: Instant,
    public val onExpire: ExpirationCallback<E>?
) : Expireable<E> {
    public var next: ExpirationNode<E>? = null; internal set
    public var previous: ExpirationNode<E>? = null; internal set
}
