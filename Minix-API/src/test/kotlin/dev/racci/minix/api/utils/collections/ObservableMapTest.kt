package dev.racci.minix.api.utils.collections

import dev.racci.minix.api.annotations.MinixExperimental
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(MinixExperimental::class)
internal class ObservableMapTest {
    private lateinit var map: ObservableMap<String, String>
    private lateinit var secondaryMap: MutableMap<String, String>

    @BeforeEach
    fun setUp() {
        map = observableMapOf()
        secondaryMap = mutableMapOf()

        map.observe { map: Map<String, String>?, key: String?, newValue: String?, oldValue: String?, action: ObservableAction ->
            println("$action: $key: $newValue -> $oldValue")
            when (action) {
                ObservableAction.ADD -> secondaryMap[key!!] = newValue!!
                ObservableAction.REMOVE -> secondaryMap.remove(key)
                ObservableAction.SET -> secondaryMap[key!!] = newValue!!
                ObservableAction.CLEAR -> secondaryMap.clear()
                ObservableAction.REPLACE -> {
                    assertNotNull(oldValue)
                    secondaryMap[key!!] = newValue!!
                }
                ObservableAction.ADD_ALL -> secondaryMap.putAll(map!!)
                else -> return@observe
            }
        }

        map["key"] = "value"
        map["key2"] = "value2"

        println(map.entries.joinToString(", ") { "${it.key}=${it.value}" })
        println(secondaryMap.entries.joinToString(", ") { "${it.key}=${it.value}" })

        assertEquals(2, map.size)
        assertEquals(2, secondaryMap.size)
    }

    @AfterEach
    fun tearDown() {
        map.clear()
    }

    @Test
    fun getListeners() {
        assertNotNull(map.listeners)
    }

    @Test
    fun clear() {
        map.clear()
        assert(map.isEmpty())
        assert(secondaryMap.isEmpty())
    }

    @Test
    fun put() {
        map["key"] = "value_other"
        assertEquals("value_other", map["key"])
        assertEquals("value_other", secondaryMap["key"])
    }

    @Test
    fun putAll() {
        map.putAll(mapOf("key" to "value_other", "key2" to "value2_other"))
        assertEquals("value_other", map["key"])
        assertEquals("value2_other", map["key2"])
        assertEquals("value_other", secondaryMap["key"])
        assertEquals("value2_other", secondaryMap["key2"])
    }

    @Test
    fun putIfAbsent() {
        map.putIfAbsent("key", "value_other")
        assertEquals("value", map["key"])
        assertEquals("value", secondaryMap["key"])
        map.putIfAbsent("key3", "value3")
        assertEquals("value3", map["key3"])
        assertEquals("value3", secondaryMap["key3"])
    }

    @Test
    fun remove() {
        map.remove("key")
        assertNull(map["key"])
        assertNull(secondaryMap["key"])
    }

    @Test
    fun replace() {
        map.replace("key", "value_other")
        assertEquals("value_other", map["key"])
        assertEquals("value_other", secondaryMap["key"])
    }

    @Test
    fun getEntries() {
        assertEquals(2, map.entries.size)
        assertEquals(2, secondaryMap.entries.size)
    }

    @Test
    fun getKeys() {
        assertEquals(2, map.keys.size)
        assertEquals(2, secondaryMap.keys.size)
    }

    @Test
    fun getSize() {
        assertEquals(2, map.size)
        assertEquals(2, secondaryMap.size)
    }

    @Test
    fun getValues() {
        assertEquals(2, map.values.size)
        assertEquals(2, secondaryMap.values.size)
    }

    @Test
    fun containsKey() {
        assertTrue(map.containsKey("key"))
        assertTrue(secondaryMap.containsKey("key"))
        assertFalse(map.containsKey("key3"))
        assertFalse(secondaryMap.containsKey("key3"))
    }

    @Test
    fun containsValue() {
        assertTrue(map.containsValue("value"))
        assertTrue(secondaryMap.containsValue("value"))
        assertFalse(map.containsValue("value3"))
        assertFalse(secondaryMap.containsValue("value3"))
    }

    @Test
    fun get() {
        assertEquals("value", map["key"])
        assertEquals("value", secondaryMap["key"])
    }

    @Test
    fun isEmpty() {
        assertFalse(map.isEmpty())
        assertFalse(secondaryMap.isEmpty())
    }
}
