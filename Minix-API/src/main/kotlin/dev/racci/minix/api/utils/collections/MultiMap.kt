@file:Suppress("Unused")

package dev.racci.minix.api.utils.collections

import com.google.common.collect.Multimap
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.ApiStatus

@Serializable
class MultiMap<K, V> internal constructor(
    @ApiStatus.Internal
    val map: MutableMap<K, MutableCollection<V>?> = mutableMapOf(),
) {

    /**
     * Returns the number of key-value pairs in this multimap.
     */
    val size: Int get() = map.size

    /**
     * Returns true if this multimap contains no key-value pairs.
     */
    val isEmpty: Boolean get() = map.isEmpty()

    /**
     * Returns a Set view of the keys contained in this multimap.
     */
    val keys: MutableSet<K> get() = map.keys

    /**
     * Returns a Set view of the mappings contained in this multimap.
     */
    val entries: MutableSet<MutableMap.MutableEntry<K, MutableCollection<V>?>> get() = map.entries

    /**
     * Returns a Collection view of Collection of the values present in
     * this multimap.
     */
    val values: MutableCollection<MutableCollection<V>?> get() = map.values

    operator fun plusAssign(value: Pair<K, MutableCollection<V>>) {
        map += value
    }

    /**
     * Add the specified value with the specified key in this multimap.
     */
    fun put(
        key: K,
        value: V,
    ) {
        if (map[key] == null) {
            map[key] = ArrayList()
        }
        map[key]!!.add(value)
    }

    /**
     * Associate the specified key with the given value if not
     * already associated with a value.
     */
    fun putIfAbsent(
        key: K,
        value: V,
    ) {
        if (map[key] == null) {
            map[key] = ArrayList()
        }

        // if the value is absent, insert it
        if (!map[key]!!.contains(value)) {
            map[key]!!.add(value)
        }
    }

    /**
     * Adds all the supplied values to this key-value.
     */
    fun addAll(
        key: K,
        vararg values: V,
    ) {
        if (map[key] == null) {
            map[key] = ArrayList()
        }

        map[key]!!.addAll(values)
    }

    /**
     * Returns the Collection of values to which the specified key is mapped,
     * or null if this multimap contains no mapping for the key.
     */
    operator fun get(key: Any?): MutableCollection<V>? = map[key]

    /**
     * Returns true if this multimap contains a mapping for the specified key.
     */
    fun containsKey(key: Any?): Boolean = map.containsKey(key)

    /**
     * Removes the mapping for the specified key from this multimap if present
     * and returns the Collection of previous values associated with key, or
     * null if there was no mapping for key.
     */
    fun remove(key: Any?): Collection<V>? = map.remove(key)

    /**
     * Returns the total number of key-value mappings in this multimap.
     */
    fun size(): Int {
        var size = 0
        for (value in map.values) {
            size += value!!.size
        }
        return size
    }

    /**
     * Removes all the mappings from this multimap.
     */
    fun clear() {
        map.clear()
    }

    /**
     * Removes the entry for the specified key only if it is currently
     * mapped to the specified value and return true if removed.
     */
    fun remove(
        key: K,
        value: V,
    ): Boolean = if (map[key] != null) {
        map[key]!!.remove(value)
        true
    } else false

    /**
     * Replaces the entry for the specified key only if currently
     * mapped to the specified value and return true if replaced.
     */
    fun replace(
        key: K,
        oldValue: V,
        newValue: V,
    ): Boolean = if (map[key] != null && map[key]!!.remove(oldValue)) {
        map[key]!!.add(newValue)
        true
    } else false

    fun forEach(block: (K, Collection<V>?) -> Unit) {
        for (entry in map.entries) {
            block.invoke(entry.key, entry.value)
        }
    }

    fun forEachIndexed(block: (index: Int, K, Collection<V>?) -> Unit) {
        for ((index, entry) in map.entries.withIndex()) {
            block.invoke(index, entry.key, entry.value)
        }
    }

    companion object {

        fun <K, V> Multimap<K, V>.toMultiMap(): MultiMap<K, V> =
            MultiMap<K, V>(this.asMap())
    }
}

fun <K, V> multiMapOf(vararg entries: Pair<K, MutableCollection<V>>): MultiMap<K, V> =
    MultiMap(mutableMapOf(*entries))
