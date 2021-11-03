package me.racci.raccicore.utils

import kotlinx.datetime.Clock
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

fun now(): Long = Instant.now().epochSecond//Clock.System.now().epochSeconds
fun nowMilli(): Long = Instant.now().toEpochMilli()
fun nowNano(): Int = Clock.System.now().nanosecondsOfSecond

@OptIn(ExperimentalTime::class)
val Long.ticks: Duration get() = toDouble().ticks
@OptIn(ExperimentalTime::class)
val Int.ticks: Duration get() = toDouble().ticks
@OptIn(ExperimentalTime::class)
val Double.ticks: Duration get() = Duration.milliseconds(tickToMilliseconds(this))

@OptIn(ExperimentalTime::class)
val Duration.inTicks: Double get() = millisecondsToTick(toDouble(DurationUnit.MILLISECONDS))

@OptIn(ExperimentalTime::class)
fun Duration.toLongTicks(): Long = inTicks.toLong()

private fun tickToMilliseconds(value: Double): Double = value * 50.0
private fun millisecondsToTick(value: Double): Double = value / 50.0