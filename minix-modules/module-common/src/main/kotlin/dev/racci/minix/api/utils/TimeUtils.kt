package dev.racci.minix.api.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

public fun now(): Instant = Clock.System.now()

public val Duration.ticks: Long get() = inWholeMilliseconds / 50

public val Int.ticks: Duration get() = (this * 50).toDuration(DurationUnit.MILLISECONDS)

public val Long.ticks: Duration get() = (this * 50).toDuration(DurationUnit.MILLISECONDS)

public val Double.ticks: Duration get() = (this * 50).toDuration(DurationUnit.MILLISECONDS)

public object TimeUtils {
    public fun processTimed(block: () -> Unit): Duration {
        val start = now()
        block()
        return now() - start
    }
}
