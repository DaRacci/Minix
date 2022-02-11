package dev.racci.minix.api.utils.kotlin

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class KotlinUtilsKtTest {

    @Test
    fun doesOverride() {
        val list = listOf<String>()
        expectThat(list).assertThat("doesOverride") {
            it.invokeIfOverrides(Collection<*>::size.name) {}
        }
    }

    @Test
    fun invokeIfNotNull() {
        var value: String? = "value"
        assertTrue(value.invokeIfNotNull {})
        value = null
        assertFalse(value.invokeIfNotNull<String?>() {})
    }

    @Test
    fun invokeIfNull() {
        var value: String? = "value"
        assertFalse(value.invokeIfNull {})
        value = null
        assertTrue(value.invokeIfNull<String?>() {})
    }

    @Test
    fun ifTrue() {
        var value: Boolean? = true
        assertTrue(value.ifTrue {})
        value = null
        assertFalse(value.ifTrue {})
    }

    @Test
    fun ifFalse() {
        var value: Boolean? = false
        assertTrue(value.ifFalse {})
        value = null
        assertFalse(value.ifFalse {})
    }
}
