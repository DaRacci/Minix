package dev.racci.minix.api.collections.expiring

import org.jetbrains.annotations.ApiStatus

/**
 * A wrapper around a function, which might be called when an element of an [ExpirationMap] expires.
 *
 * @param K The type of the key.
 * @param V The type of the value.
 */
@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public fun interface ExpirationMapCallback<K, V> {
    /**
     * Called when an element has expired and is removed from its holder.
     *
     * @param key The key of the element that has expired.
     * @param value The value of the element that has expired.
     */
    public fun onExpire(key: K, value: V)
}
