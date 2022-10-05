package dev.racci.minix.api.utils.collections.muiltimap

import kotlinx.collections.immutable.ImmutableSet

interface MultiMap<K, V> {

    /** Returns the number of key-value pairs in this multimap. */
    val size: Int

    /** Returns true if this multimap contains no key-value pairs. */
    val isEmpty: Boolean

    /** Returns a Set view of the keys contained in this multimap. */
    val keys: ImmutableSet<K>

    /** Returns a Set view of the mappings contained in this multimap. */
    val entries: ImmutableSet<Map.Entry<K, Collection<V>>>

    /** Returns a Collection view of the values present in this multimap. */
    val values: ImmutableSet<Collection<V>>

    /**
     * Returns the Collection of values to which the specified key is mapped,
     * or null if this multimap contains no mapping for the key.
     */
    operator fun get(key: K?): MutableCollection<V>?

    /** Gets the total size of all elements within this multimap. */
    fun size(): Int

    /** Returns true if this multimap contains a mapping for the specified key. */
    fun containsKey(key: Any?): Boolean

    fun forEach(block: (K, Collection<V>?) -> Unit)

    fun forEachIndexed(block: (index: Int, K, Collection<V>?) -> Unit)
}
