package dev.racci.minix.api.collections.expiring

import kotlin.time.Duration

public interface ExpirationCollection<E> : Collection<E> {

    public operator fun get(index: Int): E?

    public fun first(): E?

    public fun last(): E?

    public fun indexOf(element: E): Int

    public fun add(
        element: E,
        expireIn: Duration,
        onExpire: ExpirationCallback<E>? = null
    )

    public fun addAll(
        expireIn: Duration,
        vararg elements: E,
        onExpire: ExpirationCallback<E>? = null
    )

    public fun addAll(
        expireIn: Duration,
        vararg elements: Pair<E, ExpirationCallback<E>>
    )

    public fun addFirst(
        element: E,
        expireIn: Duration,
        onExpire: ExpirationCallback<E>? = null
    )

    public fun addLast(
        element: E,
        expireIn: Duration,
        onExpire: ExpirationCallback<E>? = null
    )

    public fun remove(element: E): Boolean

    public fun removeAt(index: Int): E?

    public fun removeFirst(): E?

    public fun removeLast(): E?

    public fun clear()
}
