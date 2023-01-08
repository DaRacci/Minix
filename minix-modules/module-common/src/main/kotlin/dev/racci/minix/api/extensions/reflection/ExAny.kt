package dev.racci.minix.api.extensions.reflection

import dev.racci.minix.api.utils.reflection.CastUtils
import dev.racci.minix.api.utils.reflection.ReflectionUtils
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KClass

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

public inline fun <T : Any> Any?.castOrThrow(
    exception: () -> Throwable
): T = this.safeCast<T>() ?: throw exception()

/** @see [CastUtils.castOrThrow] */
@Throws(ClassCastException::class)
public inline fun <T : Any> Any?.castOrThrow(): T = this as T

/** @see [CastUtils.withCast] */
public inline fun <T : Any, R> Any?.withCast(
    block: T.() -> R
): R? = this.safeCast<T>()?.block()

/** @see [ReflectionUtils.typeArgumentOf] */
@ApiStatus.Experimental
@ApiStatus.AvailableSince("5.0.0")
public inline fun <reified F : Any, T : Any> F.typeArgumentOf(
    index: Int = 0
): KClass<T> = ReflectionUtils.typeArgumentOf(this, F::class, index)
