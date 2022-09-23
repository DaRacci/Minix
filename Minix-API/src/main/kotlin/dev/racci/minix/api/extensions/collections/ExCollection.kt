package dev.racci.minix.api.extensions.collections

import arrow.core.Option
import arrow.core.Validated
import dev.racci.minix.api.utils.collections.CollectionUtils
import kotlin.reflect.KCallable

/** @see CollectionUtils.If.ifEmpty */
suspend inline fun <R, C : Collection<*>> C.ifEmpty(
    crossinline action: suspend C.() -> R
): Option<R> = CollectionUtils.If.ifEmpty(this, action)

/** @see CollectionUtils.If.ifNotEmpty */
suspend inline fun <R, C : Collection<*>> C.ifNotEmpty(
    crossinline action: suspend C.() -> R
): Option<R> = CollectionUtils.If.ifNotEmpty(this, action)

/** @see CollectionUtils.Contains.containsString */
inline fun Collection<String>.contains(
    value: String,
    ignoreCase: Boolean = false
): Boolean = CollectionUtils.Contains.containsString(this, value, ignoreCase)

/** @see CollectionUtils.Get.getCast */
inline fun <T : Any> Collection<*>.getCast(
    index: Int
): Validated<Throwable, T> = CollectionUtils.Get.getCast(this, index)

/** @see CollectionUtils.Find.findKCallable */
inline fun <T : KCallable<*>> Collection<T>.findKCallable(
    name: String,
    ignoreCase: Boolean = false
): Option<T> = CollectionUtils.Find.findKCallable(this, name, ignoreCase)

/** @see CollectionUtils.Mutate.clear */
suspend inline fun <V> MutableCollection<V>.clear(
    crossinline onRemove: suspend V.() -> Unit
): Unit = CollectionUtils.Mutate.clear(this, onRemove)
