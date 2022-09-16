@file:Suppress("NOTHING_TO_INLINE")

package dev.racci.minix.api.extensions.reflection

import dev.racci.minix.api.utils.reflection.LazyUtil
import kotlin.reflect.KProperty0

inline val KProperty0<*>.isLazy: Boolean get() = LazyUtil.isLazy(this)

inline val KProperty0<*>.isInitialised: Boolean get() = LazyUtil.isInitialised(this)

inline val <R> KProperty0<R>.orNull: R? get() = LazyUtil.getOrNull(this)

inline val <R> KProperty0<R>.orThrow: R get() = LazyUtil.getOrThrow(this)

inline fun <R> KProperty0<R>.getOrElse(
    defaultValue: () -> R
): R = LazyUtil.getOrElse(this, defaultValue)

inline fun <R> KProperty0<R>.getOrDefault(
    defaultValue: R
): R = LazyUtil.getOrDefault(this, defaultValue)

inline fun <R> KProperty0<R>.ifInitialised(
    action: R.() -> Unit
): Unit = LazyUtil.ifInitialised(this, action)
