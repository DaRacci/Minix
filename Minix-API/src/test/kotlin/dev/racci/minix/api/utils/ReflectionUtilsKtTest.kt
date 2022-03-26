package dev.racci.minix.api.utils

import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.util.UUID
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
        val constructor = TestClass::class.constructors.first().javaConstructor
        val uuid = UUID.randomUUID()
        val clazz = classConstructor(constructor!!, uuid)
        expectThat(clazz)
            .isA<TestClass>()
            .isEqualTo(TestClass(uuid))
    }

    @Test
    fun readInstanceProperty() {
        val uuid = UUID.randomUUID()
        val instance = TestClass(uuid)
        val value = readInstanceProperty<UUID>(instance, "uuid")
        expectThat(value).isEqualTo(uuid)
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

    private data class TestClass(private var uuid: UUID)
}
