package dev.racci.minix.api.utils.collections

import arrow.core.Option
import dev.racci.minix.api.extensions.reflection.safeCast

object MapUtils {

    inline fun containsKeyIgnoreCase(
        map: Map<String, *>,
        key: String,
        ignoreCase: Boolean
    ): Boolean = CollectionUtils.Contains.containsString(map.keys, key, ignoreCase)

    inline fun <V> getValueIgnoreCase(
        map: Map<String, V>,
        key: String,
        ignoreCase: Boolean
    ): Option<V> = Option.fromNullable(map.entries.firstOrNull { it.key.equals(key, ignoreCase) }?.value)

    inline fun <K, V> clear(
        map: MutableMap<K, V>,
        onRemove: (K, V) -> Unit
    ): Unit = map.entries.forEach { onRemove(it.key, it.value) }.also { map.clear() }

    inline fun <K, V> computeAndRemove(
        map: MutableMap<K, V>,
        key: K,
        crossinline onRemove: V.() -> Unit
    ): Boolean = map.computeIfPresent(key) { _, v -> v.onRemove(); null } != null

    inline fun <reified T : Any, K> getCast(
        map: Map<K, *>,
        key: K
    ): Option<T> = Option.fromNullable(map[key].safeCast())
}
