package dev.racci.minix.api.utils

import kotlinx.atomicfu.AtomicRef
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.test.assertNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CloseableTest {

    private var closeable: Closeable<Boolean>? = null

    @BeforeAll
    fun setup() {
        closeable = object : Closeable<Boolean>() {
            override fun create(): Boolean {
                return true
            }
        }
    }

    @Test
    fun `closeable should be created when gotten`() {
        assertTrue(closeable!!.get())
    }

    @Test
    fun `closeable should false once closed`() {
        closeable!!.close()
        assertNull(closeable!!::class.memberProperties.first { it.name == "value" }.also { it.isAccessible = true }.call(closeable!!).unsafeCast<AtomicRef<*>>().value)
    }
}
