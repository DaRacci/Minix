package dev.racci.minix.api.extensions.collections

import arrow.core.Option
import arrow.core.Validated
import dev.racci.minix.api.utils.collections.CollectionUtils
import kotlin.reflect.KCallable

/** @see CollectionUtils.If.ifEmpty */
public suspend inline fun <R, T> Array<T>.ifEmpty(
    crossinline action: suspend Array<T>.() -> R
): Option<R> = CollectionUtils.If.ifEmpty(this, action)

/** @see CollectionUtils.If.ifNotEmpty */
public suspend inline fun <R, T> Array<T>.ifNotEmpty(
    crossinline action: suspend Array<T>.() -> R
): Option<R> = CollectionUtils.If.ifNotEmpty(this, action)

/** @see CollectionUtils.Contains.containsString */
public inline fun Array<String>.contains(
    value: String,
    ignoreCase: Boolean = false
): Boolean = CollectionUtils.Contains.containsString(this, value, ignoreCase)

/** @see CollectionUtils.Get.getCast */
public inline fun <T : Any> Array<*>.getCast(
    index: Int
): Validated<Throwable, T> = CollectionUtils.Get.getCast(this, index)

/** @see CollectionUtils.Find.findKCallable */
public inline fun <T : KCallable<*>, R> Array<T>.findKCallable(
    name: String,
    ignoreCase: Boolean = false
): Option<KCallable<R>> = CollectionUtils.Find.findKCallable(this, name, ignoreCase)
