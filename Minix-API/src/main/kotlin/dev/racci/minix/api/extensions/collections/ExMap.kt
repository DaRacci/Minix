package dev.racci.minix.api.extensions.collections

import arrow.core.Option
import dev.racci.minix.api.utils.collections.MapUtils

/** @see MapUtils.containsKeyIgnoreCase */
inline fun Map<String, *>.contains(
    key: String,
    ignoreCase: Boolean = false
): Boolean = MapUtils.containsKeyIgnoreCase(this, key, ignoreCase)

/** @see MapUtils.getValueIgnoreCase */
inline fun <V> Map<String, V>.get(
    key: String,
    ignoreCase: Boolean = false
): Option<V> = MapUtils.getValueIgnoreCase(this, key, ignoreCase)

/** @see MapUtils.clear */
inline fun <K, V> MutableMap<K, V>.clear(
    onRemove: (K, V) -> Unit
): Unit = MapUtils.clear(this, onRemove)

/** @see MapUtils.computeAndRemove */
inline fun <K, V> MutableMap<K, V>.computeAndRemove(
    key: K,
    crossinline onRemove: V.() -> Unit
): Boolean = MapUtils.computeAndRemove(this, key, onRemove)

/** @see MapUtils.getCast */
inline fun <reified T : Any, K> Map<K, *>.getCast(
    key: K
): Option<T> = MapUtils.getCast(this, key)
