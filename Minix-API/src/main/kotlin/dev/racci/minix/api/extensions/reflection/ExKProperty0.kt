@file:Suppress("NOTHING_TO_INLINE")

package dev.racci.minix.api.extensions.reflection

import dev.racci.minix.api.utils.reflection.AccessUtils
import dev.racci.minix.api.utils.reflection.LazyUtils
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

// Lazy Extensions

/** @see LazyUtils.isLazy */
inline val KProperty0<*>.isLazy: Boolean get() = LazyUtils.isLazy(this)

/** @see LazyUtils.isInitialised */
inline val KProperty0<*>.isLazyInitialised: Boolean get() = LazyUtils.isInitialised(this)

/** @see LazyUtils.getOrNull */
inline val <R> KProperty0<R>.orNull: R? get() = LazyUtils.getOrNull(this)

/** @see LazyUtils.getOrThrow */
inline val <R> KProperty0<R>.orThrow: R get() = LazyUtils.getOrThrow(this)

/** @see LazyUtils.getOrElse */
inline fun <R> KProperty0<R>.getOrElse(
    defaultValue: () -> R
): R = LazyUtils.getOrElse(this, defaultValue)

/** @see LazyUtils.getOrDefault */
inline fun <R> KProperty0<R>.getOrDefault(
    defaultValue: R
): R = LazyUtils.getOrDefault(this, defaultValue)

/** @see LazyUtils.ifInitialised */
inline fun <R> KProperty0<R>.ifInitialised(
    action: R.() -> Unit
): Unit = LazyUtils.ifInitialised(this, action)

// Access Extensions

/** @see AccessUtils.accessGet */
inline val <R> KProperty0<R>.accessGet: R get() = AccessUtils.accessGet(this)

/** @see AccessUtils.accessSet */
inline fun <R> KProperty0<R>.accessSet(
    value: R
) { if (this is KMutableProperty0<R>) AccessUtils.accessSet(this, value) }
