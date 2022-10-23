package dev.racci.minix.api.extensions.reflection

import dev.racci.minix.api.utils.reflection.CastUtils

/** @see [CastUtils.safeCast] */
public inline fun <T : Any> Any?.safeCast(): T? = this as? T

/** @see [CastUtils.castOr] */
public inline fun <T : Any> Any?.castOr(
    default: T
): T = this.safeCast() ?: default

/** @see [CastUtils.castOrElse] */
public inline fun <T : Any> Any?.castOrElse(
    default: () -> T
): T = this.safeCast<T>() ?: default()

/** @see [CastUtils.castOrThrow] */
@Throws(ClassCastException::class)
public inline fun <T : Any> Any?.castOrThrow(): T = this as T

/** @see [CastUtils.withCast] */
public inline fun <T : Any, R> Any?.withCast(
    block: T.() -> R
): R? = this.safeCast<T>()?.block()
