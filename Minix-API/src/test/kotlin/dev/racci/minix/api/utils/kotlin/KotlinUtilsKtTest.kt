package dev.racci.minix.api.utils.kotlin

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.plugin.SusPlugin
import org.bukkit.plugin.Plugin
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isA
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class KotlinUtilsKtTest {

    @Test
    fun doesOverride() {
        val plugin = MinixPlugin()
        expectThat(plugin).assertThat("doesOverride") {
            it.invokeIfOverrides(SusPlugin::bStatsId.name) {}
        }.isA<Plugin>()
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
