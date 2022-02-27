package dev.racci.minix.api.utils.kotlin

import kotlinx.collections.immutable.persistentListOf
import kotlin.test.Test
import kotlin.test.assertEquals

internal class InfixKtTest {

    @Test
    fun and() {
        val a = 'a'
        val b = 'b'
        val c = a and b
        assertEquals(persistentListOf(a, b), c)
    }
}
