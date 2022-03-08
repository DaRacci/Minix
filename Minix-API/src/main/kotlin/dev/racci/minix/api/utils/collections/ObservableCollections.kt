@file:Suppress("UNUSED")

package dev.racci.minix.api.utils.collections

import dev.racci.minix.api.utils.kotlin.ifTrue
import kotlin.reflect.KFunction1

typealias ObservableListener<T> = (T, ObservableAction) -> Unit

enum class ObservableAction { ADD, SET, REPLACE, REMOVE, CLEAR }

fun <T> observableListOf(vararg items: T) = ObservableList(items.toMutableList())
fun <T> observableSetOf(vararg items: T) = ObservableSet(items.toMutableSet())
fun <K, V> observableMapOf(vararg items: Pair<K, V>) = ObservableMap(mutableMapOf(*items))

fun <T> observableListOf(mutableList: MutableList<T>) = mutableList.asObservable()
fun <T> observableSetOf(mutableSet: MutableSet<T>) = mutableSet.asObservable()
fun <K, V> observableMapOf(mutableMap: MutableMap<K, V>) = mutableMap.asObservable()

fun <T> MutableList<T>.asObservable() = ObservableList(this)
fun <T> MutableSet<T>.asObservable() = ObservableSet(this)
fun <K, V> MutableMap<K, V>.asObservable() = ObservableMap(this)

class ObservableList<T> internal constructor(
    private val list: MutableList<T>,
) : MutableList<T> by list, ObservableCollection<T> {

    override val listeners = multiMapOf<ObservableListener<T>, ObservableAction?>()

    override fun add(element: T): Boolean {
        runListeners { element to ObservableAction.ADD }
        return list.add(element)
    }

    override fun add(
        index: Int,
        element: T,
    ) {
        runListeners { element to ObservableAction.ADD }
        return list.add(index, element)
    }

    override fun addAll(
        index: Int,
        elements: Collection<T>,
    ): Boolean {
        elements.forEach { runListeners { it to ObservableAction.ADD } }
        return list.addAll(index, elements)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        elements.forEach { runListeners { it to ObservableAction.ADD } }
        return list.addAll(elements)
    }

    override fun clear() {
        list.forEach { runListeners { it to ObservableAction.CLEAR } }
        list.clear()
    }

    override fun listIterator(): MutableListIterator<T> = ObservableMutableListIterator(
        list.listIterator(),
        ::runListeners,
    )

    override fun listIterator(index: Int): MutableListIterator<T> = ObservableMutableListIterator(
        list.listIterator(index),
        ::runListeners
    )

    override fun remove(element: T): Boolean = list.remove(element).ifTrue {
        runListeners { element to ObservableAction.REMOVE }
    }

    override fun removeAll(elements: Collection<T>): Boolean = list.removeAll(elements.toSet()).ifTrue {
        elements.forEach { runListeners { it to ObservableAction.REMOVE } }
    }

    override fun removeAt(index: Int): T = list.removeAt(index).apply {
        runListeners { this to ObservableAction.REMOVE }
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val notIn = list.filter { it !in elements }
        notIn.forEach {
            runListeners { it to ObservableAction.REMOVE }
        }
        return notIn.isNotEmpty()
    }

    override fun set(
        index: Int,
        element: T,
    ): T = list.set(index, element).apply {
        runListeners { this to ObservableAction.SET }
    }
}

class ObservableMutableListIterator<T>(
    private val iterator: MutableListIterator<T>,
    private val runListeners: KFunction1<() -> Pair<T, ObservableAction>, Unit>,
) : MutableListIterator<T> by iterator {

    override fun add(element: T) {
        iterator.add(element)
        runListeners { element to ObservableAction.ADD }
    }

    override fun remove() {
        runListeners { iterator.next() to ObservableAction.REMOVE }
        iterator.remove()
    }

    override fun set(element: T) {
        iterator.set(element)
        runListeners { element to ObservableAction.SET }
    }
}

class ObservableSet<T> internal constructor(
    private val set: MutableSet<T>,
) : MutableSet<T> by set, ObservableCollection<T> {

    override val listeners = multiMapOf<ObservableListener<T>, ObservableAction?>()

    override fun add(element: T): Boolean = set.add(element).ifTrue {
        runListeners { element to ObservableAction.ADD }
    }

    override fun addAll(elements: Collection<T>): Boolean = set.addAll(elements).ifTrue {
        elements.forEach { runListeners { it to ObservableAction.ADD } }
    }

    override fun clear() {
        set.forEach { runListeners { it to ObservableAction.CLEAR } }
        set.clear()
    }

    override fun iterator(): MutableIterator<T> = ObservableMutableIterator(
        set.iterator(),
        ::runListeners
    )

    override fun remove(element: T): Boolean = set.remove(element).ifTrue {
        runListeners { element to ObservableAction.REMOVE }
    }

    override fun removeAll(elements: Collection<T>): Boolean = set.removeAll(elements.toSet()).ifTrue {
        elements.forEach { runListeners { it to ObservableAction.REMOVE } }
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val notIn = set.filter { it !in elements }
        notIn.forEach {
            runListeners { it to ObservableAction.REMOVE }
        }
        return notIn.isNotEmpty()
    }
}

class ObservableMutableIterator<T>(
    private val iterator: MutableIterator<T>,
    private val runListeners: KFunction1<() -> Pair<T, ObservableAction>, Unit>,
) : MutableIterator<T> by iterator {

    override fun remove() {
        runListeners { iterator.next() to ObservableAction.REMOVE }
        iterator.remove()
    }
}

class ObservableMap<K, V> internal constructor(
    private val map: MutableMap<K, V>,
) : MutableMap<K, V> by map, ObservableHolder<Pair<K, V>> {

    override val listeners = multiMapOf<ObservableListener<Pair<K, V>>, ObservableAction?>()

    override fun clear() {
        map.entries.forEach { runListeners { it.toPair() to ObservableAction.CLEAR } }
        map.clear()
    }

    override fun put(
        key: K,
        value: V,
    ): V? = map.put(key, value)?.apply {
        runListeners { key to this to ObservableAction.REMOVE }
    }

    override fun putAll(from: Map<out K, V>) {
        map.putAll(from)
        from.entries.forEach { runListeners { it.toPair() to ObservableAction.ADD } }
    }

    override fun putIfAbsent(
        key: K,
        value: V,
    ): V? = map.putIfAbsent(key, value).also {
        if (it == null) runListeners { key to value to ObservableAction.ADD }
    }

    override fun remove(key: K): V? = map.remove(key)?.also {
        runListeners { key to it to ObservableAction.REMOVE }
    }

    override fun remove(
        key: K,
        value: V,
    ): Boolean {
        val v = map.replace(key, value)
        v?.also { runListeners { key to it to ObservableAction.REMOVE } }
        return v != null
    }

    override fun replace(
        key: K,
        first: V,
        second: V,
    ): Boolean = map.replace(key, first, second).ifTrue {
        runListeners { key to second to ObservableAction.REPLACE }
    }

    override fun replace(
        key: K,
        value: V,
    ): V? = map.replace(key, value)?.also {
        runListeners { key to it to ObservableAction.REPLACE }
    }
}

interface ObservableCollection<T> : MutableCollection<T>, ObservableHolder<T>

interface ObservableHolder<T> {

    val listeners: MultiMap<ObservableListener<T>, ObservableAction?>

    fun observe(
        vararg action: ObservableAction?,
        listener: ObservableListener<T>,
    ) {
        listeners.addAll(listener, *action)
    }

    fun runListeners(action: () -> Pair<T, ObservableAction>) {
        for ((listener, actions) in listeners.entries) {
            val actionInv = action.invoke()
            if (actions != null) {
                if (actionInv.second in actions) {
                    listener.invoke(actionInv.first, actionInv.second)
                }
            } else {
                listener.invoke(actionInv.first, actionInv.second)
            }
        }
    }
}
