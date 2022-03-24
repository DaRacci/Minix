package dev.racci.minix.api.utils.collections

import dev.racci.minix.api.utils.collections.CollectionUtils.clear
import dev.racci.minix.api.utils.collections.CollectionUtils.containsIgnoreCase
import dev.racci.minix.api.utils.collections.CollectionUtils.containsKeyIgnoreCase
import dev.racci.minix.api.utils.collections.CollectionUtils.getAs
import dev.racci.minix.api.utils.collections.CollectionUtils.getAsOrNull
import dev.racci.minix.api.utils.collections.CollectionUtils.getIgnoreCase
import java.util.logging.Logger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

internal class CollectionUtilsTest {

    private val array = arrayOf("one", "two", "three", "four", "five")
    private val list = mutableListOf("one", "two", "three", "four", "five")
    private val map = mutableMapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5)

    @Test
    fun arrayTest() {
        assertTrue(array.containsIgnoreCase("ONE"))
        assertFalse(array.containsIgnoreCase("SIX"))
    }

    @Test
    fun collectionTest() {
        assertTrue(list.containsIgnoreCase("ONE"))
        assertFalse(list.containsIgnoreCase("SIX"))
        var i = 0
        list.clear {
            i++
        }
        assertEquals(5, i)
    }

    @Test
    fun mapTest() {
        assertTrue(map.containsKeyIgnoreCase("ONE"))
        assertFalse(map.containsKeyIgnoreCase("SIX"))
        assertEquals(1, map.getIgnoreCase("ONE"))
        expectThrows<ClassCastException> { map.getAs<Logger>("one") }
        assertEquals(1, map.getAs("one"))
        assertNull(map.getAsOrNull<Logger>("one"))
    }
}
