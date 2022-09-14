package dev.racci.minix.api.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun now(): Instant = Clock.System.now()

val Duration.ticks: Long get() = inWholeMilliseconds / 50

val Int.ticks: Duration get() = (this * 50).toDuration(DurationUnit.MILLISECONDS)

val Long.ticks: Duration get() = (this * 50).toDuration(DurationUnit.MILLISECONDS)

val Double.ticks: Duration get() = (this * 50).toDuration(DurationUnit.MILLISECONDS)

object TimeUtils : UtilObject by UtilObject {
    fun processTimed(block: () -> Unit): Duration {
        val start = now()
        block()
        return now() - start
    }
}
