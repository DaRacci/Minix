package dev.racci.minix.api.utils

import dev.racci.minix.api.utils.data.Data
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import kotlin.reflect.jvm.javaConstructor
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.expect

internal class ReflectionUtilsKtTest {

    @Test
    fun exists() {
        expect(true) { exists("dev.racci.minix.api.utils.data.Data") }
        expect(false) { exists("dev.racci.minix.api.utils.data") }
    }

    @Test
    fun classConstructor() {
        val constructor = Data::class.constructors.first().javaConstructor
        val clazz = classConstructor(constructor!!, 1024)
        expectThat(clazz)
            .isA<Data>()
            .isEqualTo(Data(1024))
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
