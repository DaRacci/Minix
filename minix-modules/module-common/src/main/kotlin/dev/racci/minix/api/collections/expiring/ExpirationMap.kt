package dev.racci.minix.api.collections.expiring

import org.jetbrains.annotations.ApiStatus
import kotlin.time.Duration

/**
 * The basic implementation of an expiring map.
 *
 * @param K The key type in the map.
 * @param V The value type in the map.
 */
@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
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

    /**
     * Inserts an entry into the map that will expire after [expireIn] amount of time has passed.
     *
     * @param key The key to insert.
     * @param value The value to insert.
     * @param expireIn The duration until expiration occurs.
     * @return The previous value associated with [key], or null if there was no mapping for [key].
     */
    public fun put(
        key: K,
        value: V,
        expireIn: Duration
    ): V?

    /**
     * Inserts an entry into the map that will expire after [expireIn] amount of time has passed.
     *
     * @param key The key to insert.
     * @param value The value to insert.
     * @param expireIn The duration until expiration occurs.
     * @param onExpire The [ExpirationMapCallback] to execute when the element expires.
     * @return The previous value associated with [key], or null if there was no mapping for [key].
     */
    public fun put(
        key: K,
        value: V,
        expireIn: Duration,
        onExpire: ExpirationMapCallback<K, V>?
    ): V?
}
