package dev.racci.minix.api.utils

import kotlinx.datetime.Clock
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

internal class TimeUtilsKtTest {

    @Test
    fun `now returns the same as normal getter`() {
        assertTrue { now() == Clock.System.now() }
    }

    @Test
    fun `ticks returns the milliseconds divided by 50`() {
        val duration = 50.milliseconds
        expectThat(duration.ticks)
            .isEqualTo(duration.inWholeMilliseconds / 50)
            .isEqualTo(1)
    }
}
