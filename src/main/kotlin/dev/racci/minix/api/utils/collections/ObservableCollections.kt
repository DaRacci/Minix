@file:Suppress("UNUSED")

package dev.racci.minix.api.utils.collections

typealias ObservableListener = ObservableAction.() -> Unit

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

    override val listeners = multiMapOf<ObservableListener, ObservableAction?>()

    override fun add(element: T): Boolean {
        runListeners(ObservableAction.ADD)
        return list.add(element)
    }

    override fun add(
        index: Int,
        element: T,
    ) {
        runListeners(ObservableAction.ADD)
        return list.add(index, element)
    }

    override fun addAll(
        index: Int,
        elements: Collection<T>,
    ): Boolean {
        runListeners(ObservableAction.ADD)
        return list.addAll(index, elements)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        runListeners(ObservableAction.ADD)
        return list.addAll(elements)
    }

    override fun clear() {
        list.clear()
        runListeners(ObservableAction.CLEAR)
    }

    override fun listIterator(): MutableListIterator<T> = ObservableMutableListIterator(
        list.listIterator(),
        ::runListeners
    )

    override fun listIterator(index: Int): MutableListIterator<T> = ObservableMutableListIterator(
        list.listIterator(index),
        ::runListeners
    )

    override fun remove(element: T): Boolean = list.remove(element).ifTrue {
        runListeners(ObservableAction.REMOVE)
    }

    override fun removeAll(elements: Collection<T>): Boolean = list.removeAll(elements.toSet()).ifTrue {
        runListeners(ObservableAction.REMOVE)
    }

    override fun removeAt(index: Int): T = list.removeAt(index).apply {
        runListeners(ObservableAction.REMOVE)
    }

    override fun retainAll(elements: Collection<T>): Boolean = list.retainAll(elements.toSet()).ifTrue {
        runListeners(ObservableAction.REMOVE)
    }

    override fun set(
        index: Int,
        element: T,
    ): T = list.set(index, element).apply {
        runListeners(ObservableAction.SET)
    }
}

class ObservableMutableListIterator<T>(
    private val iterator: MutableListIterator<T>,
    private val runListeners: ObservableListener,
) : MutableListIterator<T> by iterator {

    override fun add(element: T) {
        iterator.add(element)
        runListeners(ObservableAction.ADD)
    }

    override fun remove() {
        iterator.remove()
        runListeners(ObservableAction.REMOVE)
    }

    override fun set(element: T) {
        iterator.set(element)
        runListeners(ObservableAction.SET)
    }
}

class ObservableSet<T> internal constructor(
    private val set: MutableSet<T>,
) : MutableSet<T> by set, ObservableCollection<T> {

    override val listeners = multiMapOf<ObservableListener, ObservableAction?>()

    override fun add(element: T): Boolean = set.add(element).ifTrue {
        runListeners(ObservableAction.ADD)
    }

    override fun addAll(elements: Collection<T>): Boolean = set.addAll(elements).ifTrue {
        runListeners(ObservableAction.ADD)
    }

    override fun clear() {
        set.clear()
        runListeners(ObservableAction.CLEAR)
    }

    override fun iterator(): MutableIterator<T> = ObservableMutableIterator(
        set.iterator(),
        ::runListeners
    )

    override fun remove(element: T): Boolean = set.remove(element).ifTrue {
        runListeners(ObservableAction.REMOVE)
    }

    override fun removeAll(elements: Collection<T>): Boolean = set.removeAll(elements.toSet()).ifTrue {
        runListeners(ObservableAction.REMOVE)
    }

    override fun retainAll(elements: Collection<T>): Boolean = set.retainAll(elements.toSet()).ifTrue {
        runListeners(ObservableAction.REMOVE)
    }
}

class ObservableMutableIterator<T>(
    private val iterator: MutableIterator<T>,
    private val runListeners: ObservableListener,
) : MutableIterator<T> by iterator {

    override fun remove() {
        iterator.remove()
        runListeners(ObservableAction.REMOVE)
    }
}

class ObservableMap<K, V> internal constructor(
    private val map: MutableMap<K, V>,
) : MutableMap<K, V> by map, ObservableHolder {

    override val listeners = multiMapOf<ObservableListener, ObservableAction?>()

    override fun clear() {
        map.clear()
        runListeners(ObservableAction.CLEAR)
    }

    override fun put(
        key: K,
        value: V,
    ): V? = map.put(key, value).apply {
        runListeners(ObservableAction.ADD)
    }

    override fun putAll(from: Map<out K, V>) {
        map.putAll(from)
        runListeners(ObservableAction.ADD)
    }

    override fun putIfAbsent(
        key: K,
        value: V,
    ): V? = map.putIfAbsent(key, value).also {
        if (it == null) runListeners(ObservableAction.ADD)
    }

    override fun remove(key: K): V? = map.remove(key)?.also {
        runListeners(ObservableAction.REMOVE)
    }

    override fun remove(
        key: K,
        value: V,
    ): Boolean = map.remove(key, value).ifTrue {
        runListeners(ObservableAction.REMOVE)
    }

    override fun replace(
        key: K,
        first: V,
        second: V,
    ): Boolean = map.replace(key, first, second).ifTrue {
        runListeners(ObservableAction.REPLACE)
    }

    override fun replace(
        key: K,
        value: V,
    ): V? = map.replace(key, value)?.also {
        runListeners(ObservableAction.REPLACE)
    }
}

interface ObservableCollection<T> : MutableCollection<T>, ObservableHolder

interface ObservableHolder {

    val listeners: MultiMap<ObservableListener, ObservableAction?>

    fun observe(
        vararg action: ObservableAction?,
        listener: ObservableListener,
    ) {
        listeners.addAll(listener, *action)
    }

    fun runListeners(action: ObservableAction) {
        for ((listener, actions) in listeners.entries) {
            if (actions == null || action in actions) {
                listener(action)
            }
        }
    }
}

private inline fun Boolean.ifTrue(block: () -> Unit) = apply { block() }
