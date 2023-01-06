package dev.racci.minix.api.collections.expiring

import kotlinx.datetime.Instant
import org.jetbrains.annotations.ApiStatus
import kotlin.time.Duration

/**
 * A representation of a value in an [ExpirationMap].
 * This element holds the [value] itself, the [startTime], the [expireIn] time from the [startTime], and its [onExpire] callback.
 *
 * @param V The type of value in the map.
 * @property value The value.
 * @property expireIn How long from the current time the element will expire.
 * @property startTime The time the element was created/added.
 * @property onExpire The [ExpirationMapCallback] which might be executed when the element expires.
 */
@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public data class ExpirationMapValue<K, V> internal constructor(
    override val value: V,
    override val expireIn: Duration,
    override val startTime: Instant,
    val onExpire: ExpirationMapCallback<K, V>?
) : Expireable<V>
