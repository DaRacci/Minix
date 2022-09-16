@file:Suppress("NOTHING_TO_INLINE")

package dev.racci.minix.api.extensions.reflection

import dev.racci.minix.api.utils.reflection.LazyUtil
import kotlin.reflect.KProperty1

inline fun <T : Any, R> KProperty1<T, R>.isLazy(obj: T): Boolean = LazyUtil.isLazy(this, obj)

inline fun <T : Any, R> KProperty1<T, R>.isInitialised(obj: T): Boolean = LazyUtil.isInitialised(this, obj)

inline fun <T : Any, R> KProperty1<T, R>.orNull(obj: T): R? = LazyUtil.getOrNull(this, obj)

inline fun <T : Any, R> KProperty1<T, R>.orThrow(obj: T): R = LazyUtil.getOrThrow(this, obj)

inline fun <T : Any, R> KProperty1<T, R>.getOrElse(
    obj: T,
    defaultValue: () -> R
): R = LazyUtil.getOrElse(this, obj, defaultValue)

inline fun <T : Any, R> KProperty1<T, R>.getOrDefault(
    obj: T,
    defaultValue: R
): R = LazyUtil.getOrDefault(this, obj, defaultValue)

inline fun <T : Any, R> KProperty1<T, R>.ifInitialised(
    obj: T,
    action: R.() -> Unit
): Unit = LazyUtil.ifInitialised(this, obj, action)
