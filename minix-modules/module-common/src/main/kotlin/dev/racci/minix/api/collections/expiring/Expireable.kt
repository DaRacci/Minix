package dev.racci.minix.api.collections.expiring

import dev.racci.minix.api.utils.now
import kotlinx.datetime.Instant
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@ApiStatus.Internal
@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public sealed interface Expireable<V> : Delayed {
    public val value: V
    public val startTime: Instant
    public val expireIn: Duration

    /**
     * The [Duration] of time until the value expires.
     */
    public fun remainingTime(current: Instant? = null): Duration {
        if (expireIn.isNegative()) return Duration.INFINITE

        val endsAt = startTime + expireIn
        val actualCurrent = current ?: now()

        return if (actualCurrent >= endsAt) Duration.ZERO else endsAt - actualCurrent
    }

    override fun compareTo(other: Delayed?): Int {
        if (other == null) return 1
        return remainingTime().compareTo(other.getDelay(TimeUnit.MILLISECONDS).milliseconds)
    }

    override fun getDelay(unit: TimeUnit): Long {
        return remainingTime().inWholeMilliseconds
    }
}
