package dev.racci.minix.api.extensions.reflection

import dev.racci.minix.api.utils.reflection.CastUtils

/** @see [CastUtils.safeCast] */
inline fun <reified T : Any> Any?.safeCast(): T? = CastUtils.safeCast(this, T::class)

/** @see [CastUtils.castOr] */
inline fun <reified T : Any> Any?.castOr(
    default: T
): T = CastUtils.castOr(this, T::class, default)

/** @see [CastUtils.castOrElse] */
inline fun <reified T : Any> Any?.castOrElse(
    default: () -> T
): T = CastUtils.castOrElse(this, T::class, default)

/** @see [CastUtils.castOrThrow] */
@Throws(TypeCastException::class)
inline fun <reified T : Any> Any?.castOrThrow(): T = CastUtils.castOrThrow(this, T::class)

/** @see [CastUtils.withCast] */
inline fun <reified T : Any, R> Any?.withCast(
    block: T.() -> R
): R? = CastUtils.withCast(this, T::class, block)
