package dev.racci.minix.api.utils

import dev.racci.minix.api.utils.data.Data
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import kotlin.reflect.jvm.javaConstructor
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class ReflectionUtilsKtTest {

    @Test
    fun exists() {
        assertTrue(exists("dev.racci.minix.api.plugin.MinixPlugin"))
        assertFalse(exists("dev.racci.minix.api.plugin.MinixPluginn"))
    }

    @Test
    fun classConstructor() {
        val constructor = StringBuilder::class.constructors.find { it.parameters[0] == String::class }!!.javaConstructor
        val clazz = classConstructor(constructor!!, "Hello World")
        expectThat(clazz)
            .isA<StringBuilder>()
            .isEqualTo(StringBuilder("Hello World"))
    }

    @Test
    fun readInstanceProperty() {
        val instance = Data(1024)
        val value = readInstanceProperty<Long>(instance, "bytes")
        expectThat(value).isEqualTo(1024)
    }

    @Test
    fun safeCast() {
        val int = 1
        assertNotNull(int.safeCast(Number::class))
        assertNull(int.safeCast(List::class))
    }

    @Test
    fun unsafeCast() {
        val int = 1
        assertNotNull(int.unsafeCast(Number::class))
        expectThrows<ClassCastException> { int.unsafeCast(List::class) }
    }

    // TODO
    @Test
    fun testClone() {
    }
}
