package dev.racci.minix.api.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class LoadableTest {

    private var death = false

    private val loadable = object : Loadable<Boolean>() {
        override fun onLoad(): Boolean {
            if (death) error("death")
            return true
        }
    }

    @Test
    fun `loadable should be loaded`() {
        loadable.load()
        assertTrue(loadable.loaded)
    }

    @Test
    fun `loadable returns true`() {
        assertTrue(loadable.get().isSuccess)
    }

    @Test
    fun `loadable should not be loaded`() {
        loadable.unloaded
        assertTrue(!loadable.loaded)
    }

    @Test
    fun `loadable should be null on death`() {
        loadable.load()
        death = true
        loadable.unload()

        assertNull(loadable.get().getOrNull())
        assertTrue(loadable.failed)

        death = false
    }
}
