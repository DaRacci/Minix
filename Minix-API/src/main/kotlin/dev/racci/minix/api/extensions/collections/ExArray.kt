package dev.racci.minix.api.extensions.collections

import arrow.core.Option
import arrow.core.Validated
import dev.racci.minix.api.utils.collections.CollectionUtils
import kotlin.reflect.KCallable

/** @see CollectionUtils.If.ifEmpty */
inline fun <R> Array<*>?.ifEmpty(
    action: () -> R
): Option<R> = CollectionUtils.If.ifEmpty(this, action)

/** @see CollectionUtils.If.ifNotEmpty */
inline fun <R> Array<*>?.ifNotEmpty(
    action: () -> R
): Option<R> = CollectionUtils.If.ifNotEmpty(this, action)

/** @see CollectionUtils.Contains.containsString */
inline fun Array<String>.contains(
    value: String,
    ignoreCase: Boolean
): Boolean = CollectionUtils.Contains.containsString(this, value, ignoreCase)

/** @see CollectionUtils.Get.getCast */
inline fun <T : Any> Array<*>.getCast(
    index: Int
): Validated<Throwable, T> = CollectionUtils.Get.getCast(this, index)

/** @see CollectionUtils.Find.findKCallable */
inline fun <T : KCallable<*>> Array<T>.findKCallable(
    name: String,
    ignoreCase: Boolean
): Option<T> = CollectionUtils.Find.findKCallable(this, name, ignoreCase)
