package dev.racci.minix.api.utils.collections

import arrow.core.Option
import dev.racci.minix.api.extensions.reflection.safeCast
import kotlinx.coroutines.runBlocking

public object MapUtils {

    public inline fun containsKeyIgnoreCase(
        map: Map<String, *>,
        key: String,
        ignoreCase: Boolean
    ): Boolean = CollectionUtils.Contains.containsString(map.keys, key, ignoreCase)

    public inline fun <V> getValueIgnoreCase(
        map: Map<String, V>,
        key: String,
        ignoreCase: Boolean
    ): Option<V> = Option.fromNullable(map.entries.firstOrNull { it.key.equals(key, ignoreCase) }?.value)

    public inline fun <K, V> clear(
        map: MutableMap<K, V>,
        crossinline onRemove: suspend (K, V) -> Unit
    ): Unit = runBlocking { map.entries.forEach { onRemove(it.key, it.value) }.also { map.clear() } }

    public inline fun <K, V> computeAndRemove(
        map: MutableMap<K, V>,
        key: K,
        crossinline onRemove: suspend V.() -> Unit
    ): Boolean = map.computeIfPresent(key) { _, v -> runBlocking { v.onRemove() }; null } != null

    public inline fun <reified T : Any, K> getCast(
        map: Map<K, *>,
        key: K
    ): Option<T> = Option.fromNullable(map[key].safeCast())
}
