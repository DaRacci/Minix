package dev.racci.minix.api.collections.expiring

import kotlin.time.Duration

public interface ExpirationMap<K, V> : MutableMap<K, V> {

    /** This [MutableCollection] will not reflect any changes made to the map.  */
    override val values: MutableCollection<V>

    /**
     * Provides the remaining time of until the element's expiration
     *
     * @param key The key to retrieve
     * @return The duration of time remaining until expiration.
     */
    public fun remainingTime(key: K): Duration?

    /**
     * Sets the element's expiration
     *
     * @param key The key to set.
     * @param expireIn New duration until expiration occurs.
     * @return If this key was present and had its expiration changed.
     */
    public fun setExpiration(
        key: K,
        expireIn: Duration,
        onExpiration: ExpirationMapCallback<K, V>? = null
    ): Boolean

    /**
     * Sets the element's expiration callback,
     * overriding any existing callback.
     *
     * @param key The key to set.
     * @param onExpire New [Duration] until expiration occurs.
     * @return If this key was present and had its callback changed.
     */
    public fun setExpirationCallback(
        key: K,
        onExpire: ExpirationMapCallback<K, V>?
    ): Boolean

    public fun put(
        key: K,
        value: V,
        expireIn: Duration
    ): V?

    public fun put(
        key: K,
        value: V,
        expireIn: Duration,
        callback: ExpirationMapCallback<K, V>?
    ): V?
}
