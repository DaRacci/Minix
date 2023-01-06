package dev.racci.minix.api.collections.expiring

import org.apiguardian.api.API

/**
 * A wrapper around a function that might be called when an expire-able element expires.
 *
 * @param E The type of the element.
 */
@API(status = API.Status.EXPERIMENTAL, since = "5.0.0")
public fun interface ExpirationCallback<E> {
    /**
     * Called when an element has expired and is removed from its holder.
     *
     * @param element The element that has expired.
     */
    public fun onExpire(element: E)
}
