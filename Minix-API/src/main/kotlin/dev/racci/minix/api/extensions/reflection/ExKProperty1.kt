@file:Suppress("NOTHING_TO_INLINE")

package dev.racci.minix.api.extensions.reflection

import dev.racci.minix.api.utils.reflection.AccessUtils
import dev.racci.minix.api.utils.reflection.LazyUtils
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

inline fun <T : Any, R> KProperty1<T, R>.isLazy(obj: T): Boolean = LazyUtils.isLazy(this, obj)

inline fun <T : Any, R> KProperty1<T, R>.isInitialised(obj: T): Boolean = LazyUtils.isInitialised(this, obj)

inline fun <T : Any, R> KProperty1<T, R>.orNull(obj: T): R? = LazyUtils.getOrNull(this, obj)

inline fun <T : Any, R> KProperty1<T, R>.orThrow(obj: T): R = LazyUtils.getOrThrow(this, obj)

inline fun <T : Any, R> KProperty1<T, R>.getOrElse(
    obj: T,
    defaultValue: () -> R
): R = LazyUtils.getOrElse(this, obj, defaultValue)

inline fun <T : Any, R> KProperty1<T, R>.getOrDefault(
    obj: T,
    defaultValue: R
): R = LazyUtils.getOrDefault(this, obj, defaultValue)

inline fun <T : Any, R> KProperty1<T, R>.ifInitialised(
    obj: T,
    action: R.() -> Unit
): Unit = LazyUtils.ifInitialised(this, obj, action)

// Access Extensions

/** @see AccessUtils.accessGet */
inline fun <T : Any, R> KProperty1<T, R>.accessGet(
    obj: T
): R = AccessUtils.accessGet(this, obj)

/** @see AccessUtils.accessSet */
inline fun <T : Any, R> KProperty1<T, R>.accessSet(
    obj: T,
    value: R
) { if (this is KMutableProperty1<T, R>) AccessUtils.accessSet(this, obj, value) }
