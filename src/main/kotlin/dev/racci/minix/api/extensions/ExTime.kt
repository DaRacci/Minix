@file:OptIn(ExperimentalTime::class)

package dev.racci.minix.api.extensions

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

val Number.ticks: Duration get() = (toInt() * 50).milliseconds
val Duration.inWholeTicks: Long get() = inWholeMilliseconds / 50
