package dev.racci.minix.api.utils

import kotlinx.datetime.Clock
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.test.Test
import kotlin.test.expect
import kotlin.time.Duration.Companion.milliseconds

internal class TimeUtilsKtTest {

    @Test
    fun `now returns the same as normal getter`() {
        expect(now().toEpochMilliseconds()) { Clock.System.now().toEpochMilliseconds() } // We cannot match the exact nano second so this is the best we can do
    }

    @Test
    fun `ticks returns the milliseconds divided by 50`() {
        val duration = 50.milliseconds
        expectThat(duration.ticks)
            .isEqualTo(duration.inWholeMilliseconds / 50)
            .isEqualTo(1)
    }

    @Test
    fun `250 milliseconds is 5 ticks`() {
        expectThat(250.milliseconds.ticks)
            .isEqualTo(5)
    }
}
