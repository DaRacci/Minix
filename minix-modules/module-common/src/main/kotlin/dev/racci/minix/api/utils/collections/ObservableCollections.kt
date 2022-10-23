package dev.racci.minix.api.utils.collections

import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.utils.collections.muiltimap.MutableMultiMap
import dev.racci.minix.api.utils.kotlin.ifTrue
import org.apiguardian.api.API

public typealias ObservableListener<T> = (T, ObservableAction) -> Unit
public typealias ObservableListenerIndex<T> = (Int, T, ObservableAction) -> Unit

public typealias ObservableListenerCollection<T> = (Collection<T>, ObservableAction) -> Unit
public typealias ObservableListenerCollectionIndex<T> = (Collection<T>, Int, ObservableAction) -> Unit

public typealias ObservableListenerEntry<K, V> = (K, V, ObservableAction) -> Unit

public enum class ObservableAction { ADD, ADD_ALL, SET, REPLACE, REMOVE, REMOVE_ALL, CLEAR }

@API(status = API.Status.EXPERIMENTAL)
public fun <T> observableListOf(vararg items: T): ObservableList<T> = ObservableList(items.toMutableList())

@API(status = API.Status.EXPERIMENTAL)
public fun <T> observableSetOf(vararg items: T): ObservableSet<T> = ObservableSet(items.toMutableSet())

@API(status = API.Status.EXPERIMENTAL)
public fun <K, V> observableMapOf(vararg items: Pair<K, V>): ObservableMap<K, V> = ObservableMap(mutableMapOf(*items))

@API(status = API.Status.EXPERIMENTAL)
public fun <T> observableListOf(mutableList: MutableList<T>): ObservableList<T> = mutableList.asObservable()

@API(status = API.Status.EXPERIMENTAL)
public fun <T> observableSetOf(mutableSet: MutableSet<T>): ObservableSet<T> = mutableSet.asObservable()

@API(status = API.Status.EXPERIMENTAL)
public fun <K, V> observableMapOf(mutableMap: MutableMap<K, V>): ObservableMap<K, V> = mutableMap.asObservable()

@API(status = API.Status.EXPERIMENTAL)
public fun <T> MutableList<T>.asObservable(): ObservableList<T> = ObservableList(this)

@API(status = API.Status.EXPERIMENTAL)
public fun <T> MutableSet<T>.asObservable(): ObservableSet<T> = ObservableSet(this)

@API(status = API.Status.EXPERIMENTAL)
public fun <K, V> MutableMap<K, V>.asObservable(): ObservableMap<K, V> = ObservableMap(this)

@API(status = API.Status.EXPERIMENTAL)
public class ObservableList<T> internal constructor(
    private val list: MutableList<T>
) : MutableList<T> by list, ObservableCollection<T>() {

    override val listeners: MutableMultiMap<Function<Unit>, ObservableAction?> = multiMapOf()

    override fun add(element: T): Boolean {
        runListeners<ObservableListener<T>>(element, ObservableAction.ADD)
        return list.add(element)
    }

    override fun add(
        index: Int,
        element: T
    ) {
        runListeners<ObservableListenerIndex<T>>(element, index, ObservableAction.ADD)
        return list.add(index, element)
    }

    override fun addAll(
        index: Int,
        elements: Collection<T>
    ): Boolean {
        runListeners<ObservableListenerCollectionIndex<T>>(elements, index, ObservableAction.ADD_ALL)
        return list.addAll(index, elements)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        runListeners<ObservableListenerCollection<T>>(elements, ObservableAction.ADD_ALL)
        return list.addAll(elements)
    }

    public fun addAll(vararg elements: T): Boolean {
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
        element: T
    ): T = list.set(index, element).apply {
        runListeners<ObservableListenerIndex<T>>(element, index, ObservableAction.SET)
    }
}

@API(status = API.Status.EXPERIMENTAL)
public class ObservableMutableListIterator<T>(
    private val iterator: MutableListIterator<T>,
    private val runListeners: ObservableListener<T>
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

@API(status = API.Status.EXPERIMENTAL)
public class ObservableSet<T> internal constructor(
    private val set: MutableSet<T>
) : MutableSet<T> by set, ObservableCollection<T>() {

    override val listeners: MutableMultiMap<Function<Unit>, ObservableAction?> = multiMapOf()

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
        set.iterator()
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

@API(status = API.Status.EXPERIMENTAL)
public class ObservableMutableIterator<T>(
    private val iterator: MutableIterator<T>,
    private val runListeners: ObservableListener<T>
) : MutableIterator<T> by iterator {

    override fun remove() {
        runListeners(iterator.next(), ObservableAction.REMOVE)
        iterator.remove()
    }
}

@API(status = API.Status.EXPERIMENTAL)
public class ObservableMap<K, V> internal constructor(
    private val map: MutableMap<K, V>
) : MutableMap<K, V> by map, ObservableHolder<Pair<K, V>>() {

    override val listeners: MutableMultiMap<Function<Unit>, ObservableAction?> = multiMapOf()

    override fun clear() {
        runListeners<(ObservableAction) -> Unit>(ObservableAction.CLEAR)
        map.clear()
    }

    override fun put(
        key: K,
        value: V
    ): V? {
        val old = map.put(key, value)
        runListeners<ObservableListenerEntry<K, V>>(key, value, old, ObservableAction.ADD)
        return old
    }

    override fun putAll(from: Map<out K, V>) {
        map.putAll(from)
        from.entries.forEach { runListeners<ObservableListenerEntry<K, V>>(it.key, it.value, ObservableAction.ADD) }
    }

    override fun putIfAbsent(
        key: K,
        value: V
    ): V? {
        var v = get(key)
        if (v == null) {
            v = put(key, value)
            runListeners<ObservableListenerEntry<K, V>>(key, value, ObservableAction.ADD)
        }
        return v
    }

    override fun remove(key: K): V? {
        val old = map.remove(key)
        runListeners<ObservableListenerEntry<K, V>>(key, old, ObservableAction.REMOVE)
        return old
    }

    override fun remove(
        key: K,
        value: V
    ): Boolean {
        val bool = map.remove(key, value)
        bool.ifTrue { runListeners<ObservableListenerEntry<K, V>>(key, bool, ObservableAction.REMOVE) }
        return bool
    }

    override fun replace(
        key: K,
        first: V,
        second: V
    ): Boolean {
        val bool = map.replace(key, first, second)
        bool.ifTrue { runListeners<ObservableListenerEntry<K, V>>(key, bool, ObservableAction.REMOVE) }
        return bool
    }

    override fun replace(
        key: K,
        value: V
    ): V? {
        var curValue: V?
        if (get(key).also { curValue = it } != null || containsKey(key)) {
            runListeners<ObservableListenerEntry<K, V>>(key, value, curValue, ObservableAction.REMOVE)
            curValue = put(key, value)
        }
        return curValue
    }
}

@API(status = API.Status.EXPERIMENTAL)
public abstract class ObservableCollection<T> : MutableCollection<T>, ObservableHolder<T>()

@API(status = API.Status.EXPERIMENTAL)
public abstract class ObservableHolder<T> {

    public abstract val listeners: MutableMultiMap<Function<Unit>, ObservableAction?>

    public inline fun <reified F : Function<Unit>> observe(
        vararg action: ObservableAction?,
        listener: F
    ): Unit = listeners.addAll(listener, *action)

    // TODO: Improve efficiency, this is currently current inefficient and with multiple listeners it builds up a lot of garbage
    public inline fun <reified F : Function<Unit>> runListeners(vararg params: Any?) {
        for ((listener, actions) in listeners.entries) {
            if (actions.isNotEmpty() && params.last().castOrThrow() !in actions) continue

            val clazz = listener::class.java
            val method = clazz.methods.first()
            val types = method.parameters.map { it.type }
            val mut = params.filterNotNull().toMutableList()
            val args = arrayListOf<Any?>()

            // This should theoretically map values in the correct order for the function.
            while (args.size != types.size) {
                val type = types[args.size]
                val value = mut.find { type.isAssignableFrom(it::class.java) }
                args.add(value)
                mut.remove(value)
            }

            method.invoke(listener, *args.toTypedArray())
            println(10)
        }
    }
}
