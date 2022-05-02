package dev.racci.minix.api.utils.collections

import dev.racci.minix.api.utils.collections.CollectionUtils.clear
import dev.racci.minix.api.utils.collections.CollectionUtils.containsIgnoreCase
import dev.racci.minix.api.utils.collections.CollectionUtils.containsKeyIgnoreCase
import dev.racci.minix.api.utils.collections.CollectionUtils.getCast
import dev.racci.minix.api.utils.collections.CollectionUtils.getCastOrNull
import dev.racci.minix.api.utils.collections.CollectionUtils.getIgnoreCase
import strikt.api.expectCatching
import strikt.api.expectThrows
import java.util.logging.Logger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

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
        assertTrue("Map contains key ONE ignoring case") { map.containsKeyIgnoreCase("ONE") }
        assertFalse("Map doesn't contains key SIX ignoring case") { map.containsKeyIgnoreCase("SIX") }
        assertEquals(1, map.getIgnoreCase("ONE"), "Map key ONE's value is 1 ignoring case")
        assertEquals(1 as Number, map.getCast<Number>("one"), "Getting the key as a number will succeed")
        assertNull(map.getCastOrNull<Logger>("one"), "Getting the key as a logger will fail")
        expectCatching {
            expectThrows<ClassCastException> {
                map.getCast<Logger>("ONE")
            }
        }
    }
}
