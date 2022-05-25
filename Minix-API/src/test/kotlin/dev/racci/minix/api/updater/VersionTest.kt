package dev.racci.minix.api.updater

import org.junit.jupiter.api.Test
import org.koin.test.KoinTest
import strikt.api.expectThat
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class VersionTest : KoinTest {

    @Test
    fun `general properties test`() {
        val tests = arrayOf(
            "2.0.29-SNAPSHOT (build 2550)",
            "3.0.0-SNAPSHOT",
            "2.2.1-SNAPSHOT-184;d1f3ac8",
            "1.9.9-bukkit-T20220519171000-b308",
            "5.4 build 1583",
            "2.5.6-Release",
            "build 52a",
            "1.0.36-SNAPSHOT-T20220524210125"
        ).map { Version(it) }.toTypedArray()

        expectThat(tests[0])
            .assertThat("Major is 2") { it.major == 2 }
            .assertThat("Minor is 0") { it.minor == 0 }
            .assertThat("Patch is 29") { it.patch == 29 }
            .assertThat("Pre-release is true") { it.isPreRelease }
            .assertThat("Build is 2550") { it.buildNumber == 2550L }
            .assertThat("Timestamp is -1") { it.timestamp == -1L }
        expectThat(tests[1])
            .assertThat("Major is 3") { it.major == 3 }
            .assertThat("Minor is 0") { it.minor == 0 }
            .assertThat("Patch is 0") { it.patch == 0 }
            .assertThat("Pre-release is true") { it.isPreRelease }
            .assertThat("Build is -1") { it.buildNumber == -1L }
            .assertThat("Timestamp is -1") { it.timestamp == -1L }
        expectThat(tests[2])
            .assertThat("Major is 2") { it.major == 2 }
            .assertThat("Minor is 2") { it.minor == 2 }
            .assertThat("Patch is 1") { it.patch == 1 }
            .assertThat("Pre-release is true") { it.isPreRelease }
            .assertThat("Build is 184") { it.buildNumber == 184L }
            .assertThat("Timestamp is -1") { it.timestamp == -1L }
        expectThat(tests[3])
            .assertThat("Major is 1") { it.major == 1 }
            .assertThat("Minor is 9") { it.minor == 9 }
            .assertThat("Patch is 9") { it.patch == 9 }
            .assertThat("Pre-release is false") { !it.isPreRelease }
            .assertThat("Build is 308") { it.buildNumber == 308L }
            .assertThat("Timestamp is 20220519171000") { it.timestamp == 20220519171000L }
        expectThat(tests[4])
            .assertThat("Major is 5") { it.major == 5 }
            .assertThat("Minor is 4") { it.minor == 4 }
            .assertThat("Patch is 0") { it.patch == 0 }
            .assertThat("Pre-release is false") { !it.isPreRelease }
            .assertThat("Build is 1583") { it.buildNumber == 1583L }
            .assertThat("Timestamp is -1") { it.timestamp == -1L }
        expectThat(tests[5])
            .assertThat("Major is 2") { it.major == 2 }
            .assertThat("Minor is 5") { it.minor == 5 }
            .assertThat("Patch is 6") { it.patch == 6 }
            .assertThat("Pre-release is false") { !it.isPreRelease }
            .assertThat("Build is -1") { it.buildNumber == -1L }
            .assertThat("Timestamp is -1") { it.timestamp == -1L }
        expectThat(tests[6])
            .assertThat("Major is 52") { it.major == 52 }
            .assertThat("Minor is 0") { it.minor == 0 }
            .assertThat("Patch is 0") { it.patch == 0 }
            .assertThat("Pre-release is true") { it.isPreRelease }
            .assertThat("Build is -1") { it.buildNumber == -1L }
            .assertThat("Timestamp is -1") { it.timestamp == -1L }
        expectThat(tests[7])
            .assertThat("Major is 1") { it.major == 1 }
            .assertThat("Minor is 0") { it.minor == 0 }
            .assertThat("Patch is 36") { it.patch == 36 }
            .assertThat("Pre-release is true") { it.isPreRelease }
            .assertThat("Build is -1") { it.buildNumber == -1L }
            .assertThat("Timestamp is 20220524210125") { it.timestamp == 20220524210125L }
    }

    @Test
    fun testEquals() {
        val v1 = Version(5, 4, 5)
        val v2 = Version(5, 4, 5)
        assertEquals(v1, v2)
    }

    @Test
    fun compareTo() {
        var v1 = Version("5.4.5-SNAPSHOT")
        var v2 = Version("5.4.5")
        assertTrue { v1 < v2 }
        v1 = Version("5.4.5a")
        v2 = Version("5.4.5b")
        assertTrue { v1 < v2 }
        v1 = Version("5.4.5 build 50")
        v2 = Version("5.4.5 build 51")
        assertTrue { v1 < v2 }
        v1 = Version("5.4.5-T200")
        v2 = Version("5.4.5-T201")
        assertTrue { v1 < v2 }
        v1 = Version("5.4.5-SNAPSHOT")
        v2 = Version("5.4.4")
        assertTrue { v1 > v2 }
    }
}
