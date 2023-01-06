package dev.racci.minix.api.collections.expiring

import org.jetbrains.annotations.ApiStatus
import kotlin.time.Duration

/**
 * The basic interface for implementing a collection of expiring elements.
 *
 * @param E The type of element in the collection.
 */
@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public interface ExpirationCollection<E> : Collection<E> {

    /**
     * Provides the element at the given index in the collection.
     *
     * @param index The index of the element.
     * @return The element at the given index or null if the index is out of bounds.
     */
    public operator fun get(index: Int): E?

    /**
     * Provides the element at the beginning of the collection.
     *
     * @return The element at the beginning of the collection or null if the collection is empty.
     */
    public fun first(): E?

    /**
     * Provides the element at the end of the collection.
     *
     * @return The element at the end of the collection or null if the collection is empty.
     */
    public fun last(): E?

    /**
     * Provides the index of the given [element] in the collection.
     *
     * @param element The element to find the index of.
     * @return The index of the given element or -1 if the element is not in the collection.
     */
    public fun indexOf(element: E): Int

    /**
     * Adds the given [element] to the collection.
     *
     * @param element The element to add.
     * @param expireIn How long from the current time the element will expire.
     * @param onExpire The callback to call when the element expires.
     */
    public fun add(
        element: E,
        expireIn: Duration,
        onExpire: ExpirationCallback<E>? = null
    )

    /**
     * Adds all the given [elements] to the collection.
     *
     * @param elements The element to add.
     * @param expireIn How long from the current time the elements will expire.
     * @param onExpire The callback to call on each the elements once they expire.
     */
    public fun addAll(
        expireIn: Duration,
        vararg elements: E,
        onExpire: ExpirationCallback<E>? = null
    )

    /**
     * Adds the given [element] to the beginning of the collection.
     *
     * @param element The element to add.
     * @param expireIn How long from the current time the element will expire.
     * @param onExpire The callback to call when the element expires.
     */
    public fun addFirst(
        element: E,
        expireIn: Duration,
        onExpire: ExpirationCallback<E>? = null
    )

    /**
     * Adds the given [element] to the end of the collection.
     * This is the same as [add].
     *
     * @param element The element to add.
     * @param expireIn How long from the current time the element will expire.
     * @param onExpire The callback to call when the element expires.
     */
    public fun addLast(
        element: E,
        expireIn: Duration,
        onExpire: ExpirationCallback<E>? = null
    )

    /**
     * Removes the give [element] from the collection if it is present.
     *
     * @param element The element to remove.
     * @return True if the element was removed, false otherwise.
     */
    public fun remove(element: E): Boolean

    /**
     * Removes the element at the given [index] from the collection if it is present.
     *
     * @param index The index of the element to remove.
     * @return The element that was removed or null if the index was out of bounds.
     */
    public fun removeAt(index: Int): E?

    /**
     * Removes the element at the beginning of the collection if it is present.
     *
     * @return The element that was removed or null if the collection was empty.
     */
    public fun removeFirst(): E?

    /**
     * Removes the element at the end of the collection if it is present.
     *
     * @return The element that was removed or null if the collection was empty.
     */
    public fun removeLast(): E?

    /**
     * Removes all the elements from the collection, possibly calling the callback on each element.
     *
     * @param callCallbacks Whether to call the callback on each element.
     */
    public fun clear(callCallbacks: Boolean = false)
}
