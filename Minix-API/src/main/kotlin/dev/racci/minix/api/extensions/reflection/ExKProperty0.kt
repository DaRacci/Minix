@file:Suppress("NOTHING_TO_INLINE")

package dev.racci.minix.api.extensions.reflection

import dev.racci.minix.api.utils.reflection.AccessUtils
import dev.racci.minix.api.utils.reflection.LazyUtils
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

// Lazy Extensions

/** @see LazyUtils.isLazy */
suspend inline fun KProperty0<*>.isLazy(): Boolean = LazyUtils.isLazy(this)

/** @see LazyUtils.isInitialised */
suspend inline fun KProperty0<*>.isLazyInitialised(): Boolean = LazyUtils.isInitialised(this)

/** @see LazyUtils.getOrNull */
suspend inline fun <R> KProperty0<R>.orNull(): R? = LazyUtils.getOrNull(this)

/** @see LazyUtils.getOrThrow */
suspend inline fun <R> KProperty0<R>.orThrow(): R = LazyUtils.getOrThrow(this)

/** @see LazyUtils.getOrElse */
suspend inline fun <R> KProperty0<R>.getOrElse(
    defaultValue: () -> R
): R = LazyUtils.getOrElse(this, defaultValue)

/** @see LazyUtils.getOrDefault */
suspend inline fun <R> KProperty0<R>.getOrDefault(
    defaultValue: R
): R = LazyUtils.getOrDefault(this, defaultValue)

/** @see LazyUtils.ifInitialised */
suspend inline fun <R> KProperty0<R>.ifInitialised(
    action: R.() -> Unit
): Unit = LazyUtils.ifInitialised(this, action)

// Access Extensions

/** @see AccessUtils.accessGet */
suspend inline fun <R> KProperty0<R>.accessGet(): R = AccessUtils.accessGet(this)

/** @see AccessUtils.accessSet */
suspend inline fun <R> KProperty0<R>.accessSet(
    value: R
) { if (this is KMutableProperty0<R>) AccessUtils.accessSet(this, value) }
