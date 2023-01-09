package dev.racci.minix.data.delegates

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.TestTimeSource

@OptIn(ExperimentalTime::class)
class AccessWatcherTest : FunSpec({
    val testTime = TestTimeSource()
    lateinit var watcher: AccessWatcher

    beforeEach {
        watcher = AccessWatcher.of(100.milliseconds, 3, testTime)
    }

    test("AccessWatcher should return false until the accessed more than 3 times.") {
        repeat(2) {
            val bool by watcher
            bool shouldBe false
        }

        val bool by watcher
        bool shouldBe true
    }

    test("AccessWatcher should return false after the true.") {
        repeat(3) {
            val bool by watcher
        }

        val bool by watcher
        bool shouldBe false
    }

    test("AccessWatcher should return reset after the delay.") {
        repeat(2) {
            val bool by watcher
        }

        testTime += 100.milliseconds

        val bool by watcher
        bool shouldBe false
    }
})
