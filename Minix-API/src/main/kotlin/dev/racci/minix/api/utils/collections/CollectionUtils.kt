package dev.racci.minix.api.utils.collections

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.utils.UtilObject
import dev.racci.minix.api.utils.safeCast
import dev.racci.minix.api.utils.unsafeCast

/**
 * Utilities for Generic Collections.
 */
object CollectionUtils : UtilObject by UtilObject {

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
     * Get the element at this index and unsafe cast it to the specified type.
     *
     * @param T the cast type.
     * @param index the index of the element.
     * @return the casted element at this index.
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(ClassCastException::class)
    fun <T> Collection<*>.getCast(
        index: Int
    ): T = elementAtOrNull(index).unsafeCast()

    /**
     * Get the element at this index and safe cast it to the specified type.
     *
     * @param T the cast type.
     * @param index the index of the element.
     * @return the casted element at this index.
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> Collection<*>.getCastOrNull(
        index: Int
    ): T? = elementAtOrNull(index).safeCast()

    /**
     * Get the element at this index and safe cast it to the specified type.
     *
     * @param T the cast type.
     * @param index the index of the element.
     * @param def the default value if the result is null.
     * @return the casted element at this index or the default value.
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> Collection<*>.getCastOrDef(
        index: Int,
        def: () -> T
    ): T = elementAtOrNull(index).safeCast() ?: def()

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
     * Get the element at this index and unsafe cast it to the specified type.
     *
     * @param T the cast type.
     * @param index the index of the element.
     * @return the casted element at this index.
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(ClassCastException::class)
    fun <T> Array<*>.getCast(
        index: Int
    ): T = elementAtOrNull(index) as T

    /**
     * Get the element at this index and safe cast it to the specified type.
     *
     * @param T the cast type.
     * @param index the index of the element.
     * @return the casted element at this index.
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> Array<*>.getCastOrNull(
        index: Int
    ): T? = elementAtOrNull(index).safeCast()

    /**
     * Get the element at this index and safe cast it to the specified type.
     *
     * @param T the cast type.
     * @param index the index of the element.
     * @param def the default value if the result is null.
     * @return the casted element at this index or the default value.
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> Array<*>.getCastOrDef(
        index: Int,
        def: () -> T
    ): T = elementAtOrNull(index).safeCast() ?: def()

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

    /**
     * Compute an action with the item at this index and remove it from the collection.
     *
     * @param K The key type.
     * @param V The value type.
     * @param key The items key.
     * @param onRemove The action to perform.
     */
    inline fun <K, V> MutableMap<K, V>.computeAndRemove(
        key: K,
        onRemove: (K, V) -> Unit,
    ) {
        if (isEmpty() || key !in this) return
        val value = this.getOrElse(key) { return }
        onRemove(key, value)
        remove(key)
    }

    /**
     * Compute an action with the item at this index and remove it from the collection.
     *
     * @param K The key type.
     * @param V The value type.
     * @param key The items key.
     * @param onRemove The action to perform.
     */
    inline fun <K, V> MutableMap<K, V>.computeAndRemove(
        key: K,
        onRemove: V.() -> Unit,
    ) {
        if (isEmpty() || key !in this) return
        val value = this.getOrElse(key) { return }
        onRemove(value)
        remove(key)
    }

    operator fun <K, V> Map<K, V>.get(
        key: K,
        default: V,
    ): V = getOrDefault(key, default)

    inline fun <reified T> Map<*, *>.getCast(
        key: Any
    ): T = this[key].unsafeCast()

    inline fun <reified T> Map<*, *>.getCastOrNull(
        key: Any
    ): T? = this[key].safeCast()

    inline fun <reified T> Map<*, *>.getCastOrDef(
        key: Any,
        def: () -> T
    ): T = this[key].safeCast() ?: def()

    fun <K, V> cacheOf(
        build: K.() -> V,
    ): LoadingCache<K, V> = cacheOf(build) {}

    inline fun <K, V> cacheOf(
        noinline build: K.() -> V,
        builder: Caffeine<K, V>.() -> Unit
    ): LoadingCache<K, V> = Caffeine.newBuilder()
        .removalListener<K, V> { _, _, _ -> }
        .apply(builder)
        .build(build)
}
