package dev.racci.minix.api.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

fun now(): Instant = Clock.System.now()

val Duration.ticks: Long get() = inWholeMilliseconds / 50
