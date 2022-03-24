@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST")

package dev.racci.minix.api.utils.collections

/**
 * Utilities for Generic Collections.
 */
object CollectionUtils {

    /**
     * Checks if the collection contains the [String] by IgnoreCase.
     *
     * @param element The [String] to look for.
     * @return True if the collection contains the [String]
     */
    fun Collection<String>.containsIgnoreCase(
        element: String,
    ): Boolean = any { it.equals(element, true) }

    /**
     * Checks if the array contains the [String] by IgnoreCase.
     *
     * @param element The [String] to look for.
     * @return True if the array contains the [String]
     */
    fun Array<String>.containsIgnoreCase(
        element: String,
    ): Boolean = any { it.equals(element, true) }

    /**
     * Checks if the map contains the [String] as a key by IgnoreCase.
     *
     * @param V The value Type.
     * @param key The [String] to look for.
     * @return True if the map contains the key of [String]
     */
    fun <V> Map<String, V>.containsKeyIgnoreCase(
        key: String,
    ): Boolean = keys.containsIgnoreCase(key)

    /**
     * Attempts to find and retrieve the key matching the [String] by IgnoreCase.
     *
     * @param V The value Type.
     * @param key The [String] to get.
     * @return True if the map contains the key of [String]
     */
    fun <V> Map<String, V>.getIgnoreCase(
        key: String,
    ): V? = entries.find { it.key.equals(key, true) }?.value

    /**
     * Compute an action with each item of this collection
     * before removing it from the collection.
     *
     * @param T The Object Type.
     * @param onRemove The Action to execute.
     */
    inline fun <T> MutableCollection<T>.clear(
        onRemove: (T) -> Unit,
    ) {
        toMutableList().forEach {
            remove(it)
            onRemove(it)
        }
    }

    /**
     * Compute an action with each entry of this map
     * before removing it from the collection.
     *
     * @param K The key type.
     * @param V The value type.
     * @param onRemove The Action to execute.
     */
    inline fun <K, V> MutableMap<K, V>.clear(
        onRemove: (K, V) -> Unit,
    ) = keys.toMutableSet().forEach {
        onRemove(it, remove(it)!!)
    }

    /**
     * Compute an action with each entry of this map
     * before removing it from the collection.
     *
     * @param K The key type.
     * @param V The value type.
     * @param onRemove The Action to execute.
     */
    inline fun <K, V> MutableMap<K, V>.clear(
        onRemove: V.() -> Unit,
    ) = entries.toMutableSet().forEach { (key, _) ->
        onRemove(remove(key)!!)
    }

    operator fun <K, V> Map<K, V>.get(
        key: K,
        default: V,
    ) = getOrDefault(key, default)

    inline fun <reified T> Map<*, *>.getAs(key: Any): T = this[key] as T

    inline fun <reified T> Map<*, *>.getAsOrNull(key: Any): T? = with(this[key]) { if (this is T) this else null }

    inline fun <reified T> Map<*, *>.getAsOr(
        key: Any,
        def: T,
    ) = this[key] as? T ?: def
}
