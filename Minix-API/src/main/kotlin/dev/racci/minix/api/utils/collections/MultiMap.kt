package dev.racci.minix.api.utils.collections

import dev.racci.minix.api.utils.collections.muiltimap.EmptyMultiMap
import dev.racci.minix.api.utils.collections.muiltimap.MutableMultiMap

fun <K, V> multiMapOf(vararg entries: Pair<K, Collection<V>>): MutableMultiMap<K, V> = MutableMultiMap(mutableMapOf(*entries))

fun <K, V> emptyMultiMap(): dev.racci.minix.api.utils.collections.muiltimap.MultiMap<K, V> = @Suppress("UNCHECKED_CAST") (EmptyMultiMap as dev.racci.minix.api.utils.collections.muiltimap.MultiMap<K, V>)

@Deprecated("Use moved class.", ReplaceWith("MutableMultiMap<K, V", "dev.racci.minix.api.utils.collections.multimap.MutableMultiMap"))
typealias MultiMap<K, V> = MutableMultiMap<K, V>
