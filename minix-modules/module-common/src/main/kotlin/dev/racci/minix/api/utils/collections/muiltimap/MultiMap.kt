package dev.racci.minix.api.utils.collections.muiltimap

import kotlinx.collections.immutable.ImmutableSet

public interface MultiMap<K, V> {

    /** Returns the number of key-value pairs in this multimap. */
    public val size: Int

    /** Returns true if this multimap contains no key-value pairs. */
    public val isEmpty: Boolean

    /** Returns a Set view of the keys contained in this multimap. */
    public val keys: ImmutableSet<K>

    /** Returns a Set view of the mappings contained in this multimap. */
    public val entries: ImmutableSet<Map.Entry<K, Collection<V>>>

    /** Returns a Collection view of the values present in this multimap. */
    public val values: ImmutableSet<Collection<V>>

    /**
     * Returns the Collection of values to which the specified key is mapped,
     * or null if this multimap contains no mapping for the key.
     */
    public operator fun get(key: K?): MutableCollection<V>?

    /** Gets the total size of all elements within this multimap. */
    public fun size(): Int

    /** Returns true if this multimap contains a mapping for the specified key. */
    public fun containsKey(key: Any?): Boolean

    public fun forEach(block: (K, Collection<V>?) -> Unit)

    public fun forEachIndexed(block: (index: Int, K, Collection<V>?) -> Unit)
}
