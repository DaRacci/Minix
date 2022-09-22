package dev.racci.minix.api.utils.collections

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.Validated
import arrow.core.toOption
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.utils.UtilObject
import dev.racci.minix.api.utils.safeCast
import dev.racci.minix.api.utils.unsafeCast
import org.apiguardian.api.API
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

/**
 * Utilities for Generic Collections.
 */
@API(status = API.Status.MAINTAINED, since = "4.2.0")
object CollectionUtils : UtilObject by UtilObject {

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.containsIgnoreCase(element)", "dev.racci.minix.api.extensions.collections.containsIgnoreCase"))
    fun Collection<String>.containsIgnoreCase(
        element: String
    ): Boolean = any { it.equals(element, true) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.getCast(index)", "dev.racci.minix.api.extensions.collections.getCast"))
    @Throws(ClassCastException::class)
    fun <T> Collection<*>.getCast(
        index: Int
    ): T = elementAtOrNull(index).unsafeCast()

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.getCast(index)", "dev.racci.minix.api.extensions.collections.getCast"))
    inline fun <reified T> Collection<*>.getCastOrNull(
        index: Int
    ): T? = elementAtOrNull(index).safeCast()

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.getCast(index)", "dev.racci.minix.api.extensions.collections.getCast"))
    inline fun <reified T> Collection<*>.getCastOrDef(
        index: Int,
        def: () -> T
    ): T = elementAtOrNull(index).safeCast() ?: def()

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.contains(name, ignoreCase)", "dev.racci.minix.api.extensions.collections.contains"))
    fun Collection<String>.find(
        name: String,
        ignoreCase: Boolean = false
    ): String? = find { it.equals(name, ignoreCase) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.findKCallable(name, ignoreCase)", "dev.racci.minix.api.extensions.collections.findKCallable"))
    fun <T> Collection<KProperty<T>>.find(
        name: String,
        ignoreCase: Boolean = false
    ): KProperty<T>? = find { it.name.equals(name, ignoreCase) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.findKCallable(name, ignoreCase)", "dev.racci.minix.api.extensions.collections.findKCallable"))
    fun <T> Collection<KFunction<T>>.find(
        name: String,
        ignoreCase: Boolean = false
    ): KFunction<T>? = find { it.name.equals(name, ignoreCase) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    fun <T> Collection<T>.first(
        name: String,
        ignoreCase: Boolean = false,
        selector: (T) -> String
    ): T = first { selector(it).equals(name, ignoreCase) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    fun Collection<String>.first(
        name: String,
        ignoreCase: Boolean = false
    ): String = first { it.equals(name, ignoreCase) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.findKCallable(name, ignoreCase)", "dev.racci.minix.api.extensions.collections.findKCallable"))
    fun <T> Collection<KProperty<T>>.first(
        name: String,
        ignoreCase: Boolean = false
    ): KProperty<T> = first { it.name.equals(name, ignoreCase) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.findKCallable(name, ignoreCase)", "dev.racci.minix.api.extensions.collections.findKCallable"))
    fun <R : Any, T> Collection<KProperty1<R, T>>.first(
        name: String,
        ignoreCase: Boolean = false
    ): KProperty1<R, T> = first { it.name.equals(name, ignoreCase) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.findKCallable(name, ignoreCase)", "dev.racci.minix.api.extensions.collections.findKCallable"))
    fun <T> Collection<KFunction<T>>.first(
        name: String,
        ignoreCase: Boolean = false
    ): KFunction<T> = first { it.name.equals(name, ignoreCase) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    fun <T> Collection<T>.find(
        name: String,
        ignoreCase: Boolean = false,
        selector: (T) -> String
    ): T? = find { selector(it).equals(name, ignoreCase) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.contains(element, ignoreCase)", "dev.racci.minix.api.extensions.collections.contains"))
    fun Array<String>.containsIgnoreCase(
        element: String
    ): Boolean = any { it.equals(element, true) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.getCast(index)", "dev.racci.minix.api.extensions.collections.getCast"))
    @Suppress("UNCHECKED_CAST")
    @Throws(ClassCastException::class)
    fun <T> Array<*>.getCast(
        index: Int
    ): T = elementAtOrNull(index) as T

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.getCast(index)", "dev.racci.minix.api.extensions.collections.getCast"))
    inline fun <reified T> Array<*>.getCastOrNull(
        index: Int
    ): T? = elementAtOrNull(index).safeCast()

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.getCast(index)", "dev.racci.minix.api.extensions.collections.getCast"))
    inline fun <reified T> Array<*>.getCastOrDef(
        index: Int,
        def: () -> T
    ): T = elementAtOrNull(index).safeCast() ?: def()

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.contains(name, ignoreCase)", "dev.racci.minix.api.extensions.collections.contains"))
    fun Array<String>.find(
        name: String,
        ignoreCase: Boolean = false
    ): String? = find { it.equals(name, ignoreCase) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.findKCallable(name, ignoreCase)", "dev.racci.minix.api.extensions.collections.findKCallable"))
    fun <T> Array<KProperty<T>>.find(
        name: String,
        ignoreCase: Boolean = false
    ): KProperty<T>? = find { it.name.equals(name, ignoreCase) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.findKCallable(name, ignoreCase)", "dev.racci.minix.api.extensions.collections.findKCallable"))
    fun <T> Array<KFunction<T>>.find(
        name: String,
        ignoreCase: Boolean = false
    ): KFunction<T>? = find { it.name.equals(name, ignoreCase) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))

    fun <T> Array<T>.find(
        name: String,
        ignoreCase: Boolean = false,
        selector: (T) -> String
    ): T? = find { selector(it).equals(name, ignoreCase) }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use moved function", ReplaceWith("this.findKCallable(name, ignoreCase)", "dev.racci.minix.api.extensions.collections.findKCallable"))
    fun <V> Map<String, V>.containsKeyIgnoreCase(
        key: String
    ): Boolean = keys.containsIgnoreCase(key)

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    fun <V> Map<String, V>.getIgnoreCase(
        key: String
    ): V? = entries.find { it.key.equals(key, true) }?.value

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    inline fun <T> MutableCollection<T>.clear(
        onRemove: (T) -> Unit
    ) {
        toMutableList().forEach {
            remove(it)
            onRemove(it)
        }
    }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    inline fun <K, V> MutableMap<K, V>.clear(
        onRemove: (K, V) -> Unit
    ) = keys.toMutableSet().forEach {
        onRemove(it, remove(it)!!)
    }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    inline fun <K, V> MutableMap<K, V>.clear(
        onRemove: V.() -> Unit
    ) = entries.toMutableSet().forEach { (key, _) ->
        onRemove(remove(key)!!)
    }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    inline fun <K, V> MutableMap<K, V>.computeAndRemove(
        key: K,
        onRemove: (K, V) -> Unit
    ): Boolean {
        if (isEmpty() || key !in this) return false
        val value = this[key] ?: return false

        onRemove(key, value)
        return remove(key) != null
    }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    inline fun <K, V> MutableMap<K, V>.computeAndRemove(
        key: K,
        onRemove: V.() -> Unit
    ): Boolean {
        if (isEmpty() || key !in this) return false
        val value = this[key] ?: return false

        onRemove(value)
        return remove(key) != null
    }

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    operator fun <K, V> Map<K, V>.get(
        key: K,
        default: V
    ): V = getOrDefault(key, default)

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    inline fun <reified T> Map<*, *>.getCast(
        key: Any
    ): T = this[key].unsafeCast()

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    inline fun <reified T> Map<*, *>.getCastOrNull(
        key: Any
    ): T? = this[key].safeCast()

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    inline fun <reified T> Map<*, *>.getCastOrDef(
        key: Any,
        def: () -> T
    ): T = this[key].safeCast() ?: def()

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    fun <K, V> cacheOf(
        build: K.() -> V
    ): LoadingCache<K, V> = cacheOf(build) {}

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("", ReplaceWith(""))
    inline fun <K, V> cacheOf(
        noinline build: K.() -> V,
        builder: Caffeine<K, V>.() -> Unit
    ): LoadingCache<K, V> = Caffeine.newBuilder()
        .removalListener<K, V> { _, _, _ -> }
        .apply(builder)
        .build(build)

    object If {
        @JvmName("ifEmptyCollection")
        inline fun <R> ifEmpty(
            collection: Collection<*>?,
            action: () -> R
        ): Option<R> = if (!collection.isNullOrEmpty()) Some(action()) else None

        @JvmName("ifEmptyArray")
        inline fun <R> ifEmpty(
            collection: Array<*>?,
            action: () -> R
        ): Option<R> = if (!collection.isNullOrEmpty()) Some(action()) else None

        @JvmName("ifNotEmptyCollection")
        inline fun <R> ifNotEmpty(
            collection: Collection<*>?,
            action: () -> R
        ): Option<R> = if (collection.isNullOrEmpty()) Some(action()) else None

        @JvmName("ifNotEmptyArray")
        inline fun <R> ifNotEmpty(
            collection: Array<*>?,
            action: () -> R
        ): Option<R> = if (collection.isNullOrEmpty()) Some(action()) else None
    }

    object Contains {
        @JvmName("containsStringCollection")
        inline fun containsString(
            collection: Collection<String>,
            value: String,
            ignoreCase: Boolean
        ): Boolean = collection.any { it.equals(value, ignoreCase) }

        @JvmName("containsStringArray")
        inline fun containsString(
            collection: Array<String>,
            value: String,
            ignoreCase: Boolean
        ): Boolean = collection.any { it.equals(value, ignoreCase) }
    }

    object Get {
        @JvmName("getCastCollection")
        inline fun <T : Any> getCast(
            collection: Collection<*>,
            index: Int
        ): Validated<Throwable, T> = Validated.catch { collection.elementAt(index).castOrThrow() }

        @JvmName("getCastArray")
        inline fun <T : Any> getCast(
            collection: Array<*>,
            index: Int
        ): Validated<Throwable, T> = Validated.catch { collection.elementAt(index).castOrThrow() }
    }

    object Find {
        @JvmName("findKCallableCollection")
        fun <T : KCallable<*>> findKCallable(
            collection: Collection<T>,
            name: String,
            ignoreCase: Boolean
        ): Option<T> = collection.find { it.name.equals(name, ignoreCase) }.toOption()

        @JvmName("findKCallableArray")
        fun <T : KCallable<*>> findKCallable(
            collection: Array<T>,
            name: String,
            ignoreCase: Boolean
        ): Option<T> = collection.find { it.name.equals(name, ignoreCase) }.toOption()
    }
}
