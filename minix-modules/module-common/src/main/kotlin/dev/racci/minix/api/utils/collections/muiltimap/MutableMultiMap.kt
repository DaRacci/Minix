package dev.racci.minix.api.utils.collections.muiltimap

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.serialization.Serializable

@Serializable
public class MutableMultiMap<K, V> : MultiMap<K, V> {
    internal constructor(map: Map<K, Collection<V>>) {
        this.internalMap = LinkedHashMap(map.mapValues { LinkedHashSet(it.value) })
    }

    private val internalMap: MutableMap<K, MutableSet<V>>

    override val size: Int get() = internalMap.size

    override val isEmpty: Boolean get() = internalMap.isEmpty()

    override val keys: ImmutableSet<K> get() = internalMap.keys.toImmutableSet()

    override val entries: ImmutableSet<MutableMap.MutableEntry<K, MutableSet<V>>> get() = internalMap.entries.toImmutableSet()

    override val values: ImmutableSet<MutableSet<V>> get() = internalMap.values.toImmutableSet()

    /** Returns the Collection of values to which the specified key is mapped, or null if this multimap contains no mapping for the key. */
    override operator fun get(key: K?): MutableCollection<V>? = internalMap[key]

    /** Adds the specified value with the specified key in this multimap. */
    public operator fun plusAssign(value: Pair<K, V>) { this.put(value.first, value.second) }

    /** Removes the specified value with the specified key in this multimap. */
    public operator fun minusAssign(value: Pair<K, V>) { this.remove(value.first, value.second) }

    /** Removes this key (and its corresponding value) from this multimap. */
    public operator fun minusAssign(key: K) { this.remove(key) }

    override fun size(): Int {
        var size = 0
        for (value in internalMap.values) {
            size += value.size
        }
        return size
    }

    /** Add the specified value with the specified key in this multimap. */
    public fun put(
        key: K,
        value: V
    ) {
        this.ensureHas(key)
        internalMap[key]!!.add(value)
    }

    /** Associate the specified key with the given value if this key is not present within the multimap. */
    public fun putIfAbsent(
        key: K,
        value: V
    ) {
        this.ensureHas(key)

        // if the value is absent, insert it
        if (!internalMap[key]!!.contains(value)) {
            internalMap[key]!!.add(value)
        }
    }

    /** Adds all the supplied values to this key-value. */
    public fun addAll(
        key: K,
        vararg values: V
    ) {
        this.ensureHas(key)

        internalMap[key]!!.addAll(values)
    }

    override fun containsKey(key: Any?): Boolean = internalMap.containsKey(key)

    /**
     * Removes the mapping for the specified key from this multimap if present
     * and returns the Collection of previous values associated with key, or
     * null if there was no mapping for key.
     */
    public fun remove(key: Any?): ImmutableSet<V>? = internalMap.remove(key)?.toImmutableSet()

    /** Removes all the mappings from this multimap. */
    public fun clear() {
        internalMap.clear()
    }

    /**
     * Clears this multimap of all mappings.
     *
     * @param onRemove The action to run on each key with its values.
     */
    public fun clear(onRemove: (K, V) -> Unit) {
        internalMap.entries.flatMap { entry ->
            entry.value.map { value -> entry.key to value }
        }.forEach { (key, value) -> onRemove(key, value) }
    }

    /**
     * Removes the entry for the specified key only if it is currently
     * mapped to the specified value and return true if removed.
     */
    public fun remove(
        key: K,
        value: V
    ): Boolean = if (internalMap[key] != null) {
        internalMap[key]!!.remove(value)
        true
    } else false

    /**
     * Replaces the entry for the specified key only if currently
     * mapped to the specified value and return true if replaced.
     */
    public fun replace(
        key: K,
        oldValue: V,
        newValue: V
    ): Boolean = if (internalMap[key] != null && internalMap[key]!!.remove(oldValue)) {
        internalMap[key]!!.add(newValue)
        true
    } else false

    /**
     * Removes the mapping for the specified key from the multimap if present.
     *
     * @param key key whose mapping is to be removed from the multimap.
     * @param onRemove The action to run on each value of the key.
     * @return If the multimap has changed as a result of this call.
     */
    public fun remove(
        key: Any?,
        onRemove: (V) -> Unit
    ): Boolean {
        if (internalMap[key].isNullOrEmpty()) return false
        internalMap[key]!!.forEach { onRemove(it) }
        internalMap.remove(key)
        return true
    }

    override fun forEach(block: (K, Collection<V>?) -> Unit) {
        for (entry in internalMap.entries) {
            block.invoke(entry.key, entry.value)
        }
    }

    override fun forEachIndexed(block: (index: Int, K, Collection<V>?) -> Unit) {
        for ((index, entry) in internalMap.entries.withIndex()) {
            block.invoke(index, entry.key, entry.value)
        }
    }

    private fun ensureHas(key: K) {
        if (internalMap[key] == null) {
            internalMap[key] = mutableSetOf()
        }
    }
}
