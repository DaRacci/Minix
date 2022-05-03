package dev.racci.minix.api.utils.collections

import dev.racci.minix.api.annotations.MinixExperimental
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(MinixExperimental::class)
internal class ObservableListTest {

    private lateinit var list: ObservableList<Int>
    private lateinit var secondaryList: MutableList<Int>

    @BeforeEach
    internal fun setUp() {
        secondaryList = mutableListOf()
        list = observableListOf()
        list.observe { collection: Collection<Int>?, int: Int?, index: Int?, action: ObservableAction ->
            when (action) {
                ObservableAction.ADD -> if (index != null) secondaryList.add(index, int!!) else secondaryList.add(int!!)
                ObservableAction.REMOVE -> if (index != null) secondaryList.removeAt(index) else secondaryList.remove(int!!)
                ObservableAction.REPLACE -> secondaryList[index!!] = int!!
                ObservableAction.SET -> secondaryList[index!!] = int!!
                ObservableAction.CLEAR -> secondaryList.clear()
                ObservableAction.ADD_ALL -> secondaryList.addAll(collection!!)
                ObservableAction.REMOVE_ALL -> secondaryList.removeAll(collection!!)
            }
        }
        list.addAll(1, 2, 3)
    }

    @AfterEach
    internal fun tearDown() {
        list.clear()
        secondaryList.clear()
    }

    @Test
    fun time() {
        val now = System.currentTimeMillis()

        repeat(100) {
            list.add(4)
            list.addAll(5, 6, 7)
            list.remove(3)
            list.removeAt(1)
            list.set(0, 8)
            list.clear()
        }

        println(System.currentTimeMillis() - now)
    }

    @Test
    fun getListeners() {
        assertEquals(1, list.listeners.size)
    }

    @Test
    fun add() {
        list.add(4)
        assertEquals(4, list.last())
        assertEquals(4, secondaryList.last())
    }

    @Test
    fun addAll() {
        list.addAll(listOf(4, 5, 6))
        assertEquals(6, list.last())
        assertEquals(list.sum(), secondaryList.sum())
    }

    @Test
    fun clear() {
        list.clear()
        assertEquals(0, list.size)
        assertEquals(0, secondaryList.size)
    }

    @Test
    fun listIterator() {
        val iterator = list.listIterator()
        assertEquals(1, iterator.next())
        assertEquals(2, iterator.next())
        assertEquals(3, iterator.next())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun remove() {
        list.remove(2)
        assertEquals(3, list[1])
        assertEquals(3, secondaryList[1])
    }

    @Test
    fun removeAll() {
        list.removeAll(listOf(1, 2))
        assertEquals(1, list.size)
        assertEquals(1, secondaryList.size)
    }

    @Test
    fun removeAt() {
        list.removeAt(0)
        assertEquals(2, list.size)
        assertEquals(2, secondaryList.size)
    }

    @Test
    fun retainAll() {
        list.retainAll(listOf(1, 2))
        assertEquals(2, list.size)
        assertEquals(2, secondaryList.size)
    }

    @Test
    fun set() {
        list.set(0, 4)
        assertEquals(4, list.first())
        assertEquals(4, secondaryList.first())
    }

    @Test
    fun getSize() {
        assertEquals(3, list.size)
    }

    @Test
    fun contains() {
        assertTrue(list.contains(1))
        assertFalse(list.contains(4))
    }

    @Test
    fun containsAll() {
        assertTrue(list.containsAll(listOf(1, 2)))
        assertFalse(list.containsAll(listOf(1, 2, 3, 4)))
    }

    @Test
    fun get() {
        assertEquals(1, list.get(0))
    }

    @Test
    fun indexOf() {
        assertEquals(0, list.indexOf(1))
        assertEquals(-1, list.indexOf(4))
    }

    @Test
    fun isEmpty() {
        assertFalse(list.isEmpty())
    }

    @Test
    fun lastIndexOf() {
        assertEquals(2, list.lastIndexOf(3))
        assertEquals(-1, list.lastIndexOf(4))
    }

    @Test
    fun subList() {
        val subList = list.subList(0, 2)
        assertEquals(listOf(1, 2), subList)
    }
}
