package dev.racci.minix.api.utils.collections

import dev.racci.minix.api.annotations.MinixExperimental
import dev.racci.minix.api.utils.kotlin.ifTrue
import dev.racci.minix.api.utils.unsafeCast

typealias ObservableListener<T> = (T, ObservableAction) -> Unit
typealias ObservableListenerIndex<T> = (Int, T, ObservableAction) -> Unit

typealias ObservableListenerCollection<T> = (Collection<T>, ObservableAction) -> Unit
typealias ObservableListenerCollectionIndex<T> = (Collection<T>, Int, ObservableAction) -> Unit

typealias ObservableListenerEntry<K, V> = (K, V, ObservableAction) -> Unit

enum class ObservableAction { ADD, ADD_ALL, SET, REPLACE, REMOVE, REMOVE_ALL, CLEAR }

@MinixExperimental fun <T> observableListOf(vararg items: T) = ObservableList(items.toMutableList())
@MinixExperimental fun <T> observableSetOf(vararg items: T) = ObservableSet(items.toMutableSet())
@MinixExperimental fun <K, V> observableMapOf(vararg items: Pair<K, V>) = ObservableMap(mutableMapOf(*items))

@MinixExperimental fun <T> observableListOf(mutableList: MutableList<T>) = mutableList.asObservable()
@MinixExperimental fun <T> observableSetOf(mutableSet: MutableSet<T>) = mutableSet.asObservable()
@MinixExperimental fun <K, V> observableMapOf(mutableMap: MutableMap<K, V>) = mutableMap.asObservable()

@MinixExperimental fun <T> MutableList<T>.asObservable() = ObservableList(this)
@MinixExperimental fun <T> MutableSet<T>.asObservable() = ObservableSet(this)
@MinixExperimental fun <K, V> MutableMap<K, V>.asObservable() = ObservableMap(this)

@MinixExperimental
class ObservableList<T> internal constructor(
    private val list: MutableList<T>,
) : MutableList<T> by list, ObservableCollection<T>() {

    override val listeners = multiMapOf<Function<Unit>, ObservableAction?>()

    override fun add(element: T): Boolean {
        runListeners<ObservableListener<T>>(element, ObservableAction.ADD)
        return list.add(element)
    }

    override fun add(
        index: Int,
        element: T,
    ) {
        runListeners<ObservableListenerIndex<T>>(element, index, ObservableAction.ADD)
        return list.add(index, element)
    }

    override fun addAll(
        index: Int,
        elements: Collection<T>,
    ): Boolean {
        runListeners<ObservableListenerCollectionIndex<T>>(elements, index, ObservableAction.ADD_ALL)
        return list.addAll(index, elements)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        runListeners<ObservableListenerCollection<T>>(elements, ObservableAction.ADD_ALL)
        return list.addAll(elements)
    }

    fun addAll(vararg elements: T): Boolean {
        runListeners<ObservableListenerCollection<T>>(elements.toSet(), ObservableAction.ADD_ALL)
        return list.addAll(elements)
    }

    override fun clear() {
        runListeners<(ObservableAction) -> Unit>(ObservableAction.CLEAR)
        list.clear()
    }

    override fun listIterator(): MutableListIterator<T> = ObservableMutableListIterator(
        list.listIterator()
    ) { value, action -> runListeners<ObservableListener<T>>(value, action) }

    override fun listIterator(index: Int): MutableListIterator<T> = ObservableMutableListIterator(
        list.listIterator(index)
    ) { value, action -> runListeners<ObservableListener<T>>(value, action) }

    override fun remove(element: T): Boolean = list.remove(element).ifTrue {
        runListeners<ObservableListener<T>>(element, ObservableAction.REMOVE)
    }

    override fun removeAll(elements: Collection<T>): Boolean = list.removeAll(elements.toSet()).ifTrue {
        runListeners<ObservableListenerCollection<T>>(elements, ObservableAction.REMOVE_ALL)
    }

    override fun removeAt(index: Int): T = list.removeAt(index).apply {
        runListeners<ObservableListenerIndex<T>>(this, index, ObservableAction.REMOVE)
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val notIn = list.filter { it !in elements }
        runListeners<ObservableListenerCollection<T>>(notIn, ObservableAction.REMOVE_ALL)
        return list.retainAll(elements)
    }

    override fun set(
        index: Int,
        element: T,
    ): T = list.set(index, element).apply {
        runListeners<ObservableListenerIndex<T>>(element, index, ObservableAction.SET)
    }
}

@MinixExperimental
class ObservableMutableListIterator<T>(
    private val iterator: MutableListIterator<T>,
    private val runListeners: ObservableListener<T>,
) : MutableListIterator<T> by iterator {

    override fun add(element: T) {
        iterator.add(element)
        runListeners(element, ObservableAction.ADD)
    }

    override fun remove() {
        runListeners(iterator.next(), ObservableAction.REMOVE)
        iterator.remove()
    }

    override fun set(element: T) {
        iterator.set(element)
        runListeners(element, ObservableAction.SET)
    }
}

@MinixExperimental
class ObservableSet<T> internal constructor(
    private val set: MutableSet<T>,
) : MutableSet<T> by set, ObservableCollection<T>() {

    override val listeners = multiMapOf<Function<Unit>, ObservableAction?>()

    override fun add(element: T): Boolean = set.add(element).ifTrue {
        runListeners<ObservableListener<T>>(element, ObservableAction.ADD)
    }

    override fun addAll(elements: Collection<T>): Boolean = set.addAll(elements).ifTrue {
        runListeners<ObservableListenerCollection<T>>(elements, ObservableAction.ADD_ALL)
    }

    override fun clear() {
        runListeners<(ObservableAction) -> Unit>(ObservableAction.CLEAR)
        set.clear()
    }

    override fun iterator(): MutableIterator<T> = ObservableMutableIterator(
        set.iterator(),
    ) { value, action -> runListeners<ObservableListener<T>>(value, action) }

    override fun remove(element: T): Boolean = set.remove(element).ifTrue {
        runListeners<ObservableListener<T>>(element, ObservableAction.REMOVE)
    }

    override fun removeAll(elements: Collection<T>): Boolean = set.removeAll(elements.toSet()).ifTrue {
        runListeners<ObservableListenerCollection<T>>(elements, ObservableAction.REMOVE)
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val notIn = set.filter { it !in elements }
        runListeners<ObservableListenerCollection<T>>(notIn, ObservableAction.REMOVE_ALL)
        return set.retainAll(elements.toSet())
    }
}

@MinixExperimental
class ObservableMutableIterator<T>(
    private val iterator: MutableIterator<T>,
    private val runListeners: ObservableListener<T>,
) : MutableIterator<T> by iterator {

    override fun remove() {
        runListeners(iterator.next(), ObservableAction.REMOVE)
        iterator.remove()
    }
}

@MinixExperimental
class ObservableMap<K, V> internal constructor(
    private val map: MutableMap<K, V>,
) : MutableMap<K, V> by map, ObservableHolder<Pair<K, V>>() {

    override val listeners = multiMapOf<Function<Unit>, ObservableAction?>()

    override fun clear() {
        runListeners<(ObservableAction) -> Unit>(ObservableAction.CLEAR)
        map.clear()
    }

    override fun put(
        key: K,
        value: V,
    ): V? = map.put(key, value)?.apply {
        runListeners<ObservableListenerEntry<K, V>>(key, this, ObservableAction.REMOVE)
    }

    override fun putAll(from: Map<out K, V>) {
        map.putAll(from)
        from.entries.forEach { runListeners<ObservableListenerEntry<K, V>>(it.key, it.value, ObservableAction.ADD) }
    }

    override fun putIfAbsent(
        key: K,
        value: V,
    ): V? = map.putIfAbsent(key, value)?.also {
        runListeners<ObservableListenerEntry<K, V>>(key, value, ObservableAction.ADD)
    }

    override fun remove(key: K): V? = map.remove(key)?.also {
        runListeners<ObservableListenerEntry<K, V>>(key, it, ObservableAction.REMOVE)
    }

    override fun remove(
        key: K,
        value: V,
    ): Boolean = map.remove(key, value).ifTrue {
        runListeners<ObservableListenerEntry<K, V>>(key, value, ObservableAction.REMOVE)
    }

    override fun replace(
        key: K,
        first: V,
        second: V,
    ): Boolean = map.replace(key, first, second).ifTrue {
        runListeners<ObservableListenerEntry<K, V>>(key, second, ObservableAction.REPLACE)
    }

    override fun replace(
        key: K,
        value: V,
    ): V? = map.replace(key, value)?.also {
        runListeners<ObservableListenerEntry<K, V>>(key, it, ObservableAction.REPLACE)
    }
}

@MinixExperimental
abstract class ObservableCollection<T> : MutableCollection<T>, ObservableHolder<T>()

@MinixExperimental
abstract class ObservableHolder<T> {

    abstract val listeners: MultiMap<Function<Unit>, ObservableAction?>

    inline fun <reified F : Function<Unit>> observe(
        vararg action: ObservableAction?,
        listener: F,
    ) = listeners.addAll(listener, *action)

    // TODO: Improve efficiency, this is currently current inefficient and with multiple listeners it builds up a lot of garbage
    inline fun <reified F : Function<Unit>> runListeners(vararg params: Any?) {
        for ((listener, actions) in listeners.entries) {
            if (!actions.isNullOrEmpty() && params.last().unsafeCast() !in actions) continue

            val clazz = listener::class.java
            val types = clazz.methods.first().parameters.map { it.type }
            val mut = params.filterNotNull().toMutableList()
            val args = arrayListOf<Any?>()

            // This should theoretically map values in the correct order for the function.
            while (args.size != types.size) {
                val type = types[args.size]
                val value = mut.find { type.isAssignableFrom(it::class.java) }
                args.add(value)
                mut.remove(value)
            }

            listener::class.java.methods.first().invoke(listener, *args.toTypedArray())
        }
    }
}
