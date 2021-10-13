package me.racci.raccicore.utils

import kotlin.time.*

fun now(): Long = System.currentTimeMillis()
fun nowNano(): Long = System.nanoTime()

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