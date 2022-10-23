package dev.racci.minix.api.collections.expiring

import dev.racci.minix.api.utils.now
import kotlinx.datetime.Instant
import kotlin.time.Duration

public data class ExpirationMapValue<K, V>(
    val value: V,
    val expireIn: Duration,
    val startTime: Instant,
    val onExpire: ExpirationMapCallback<K, V>?
) {
    public fun remainingTime(): Duration {
        if (expireIn.isNegative()) return Duration.INFINITE

        val endsAt = startTime + expireIn
        val current = now()

        return if (current >= endsAt) Duration.ZERO else endsAt - current
    }
}
