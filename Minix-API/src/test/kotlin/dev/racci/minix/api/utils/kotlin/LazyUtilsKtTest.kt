package dev.racci.minix.api.utils.kotlin

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class LazyUtilsKtTest {

    @Test
    fun ifInitialized() {
        val lazy = lazy { "foo" }
        var bool = false
        lazy.ifInitialized { bool = true }
        assertFalse(bool)
        lazy.value
        lazy.ifInitialized { bool = true }
        assertTrue(bool)
    }
}
