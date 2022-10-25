package dev.racci.minix.api.utils.collections

import dev.racci.minix.api.utils.collections.muiltimap.EmptyMultiMap
import dev.racci.minix.api.utils.collections.muiltimap.MultiMap
import dev.racci.minix.api.utils.collections.muiltimap.MutableMultiMap

public fun <K, V> multiMapOf(vararg entries: Pair<K, Collection<V>>): MutableMultiMap<K, V> = MutableMultiMap(mutableMapOf(*entries))

public fun <K, V> emptyMultiMap(): MultiMap<K, V> = @Suppress("UNCHECKED_CAST") (EmptyMultiMap as MultiMap<K, V>)
