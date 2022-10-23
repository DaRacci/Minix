package dev.racci.minix.api.collections.expiring

public fun interface ExpirationCallback<E> {
    public fun onExpire(element: E)
}
