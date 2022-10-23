package dev.racci.minix.api.utils.reflection

import org.apiguardian.api.API
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.safeCast

@Suppress("NOTHING_TO_INLINE")
@API(status = API.Status.EXPERIMENTAL, since = "4.1.0")
public object CastUtils {

    /**
     * Casts the given [obj] to [T] or returns null if the cast fails.
     *
     * @param obj The object to cast.
     * @param kClass The class to cast to.
     * @param T The type to return.
     * @return The cast object or null.
     */
    @JvmName("safeCast")
    public inline fun <T : Any> safeCast(
        obj: Any?,
        kClass: KClass<T>
    ): T? = kClass.safeCast(obj)

    /**
     * Casts the given [obj] to [T] or returns [default] if the cast fails.
     *
     * @param obj The object to cast.
     * @param kClass The class to cast to.
     * @param default The default value to return if the cast fails.
     * @param T The type to return.
     * @return The cast object or [default].
     */
    @JvmName("castOr")
    public inline fun <T : Any> castOr(
        obj: Any?,
        kClass: KClass<T>,
        default: T
    ): T = safeCast(obj, kClass) ?: default

    /**
     * Casts the given [obj] to [T] or returns the [default] if the cast fails.
     *
     * @param obj The object to cast.
     * @param kClass The class to cast to.
     * @param default The default value to get and return if the cast fails.
     * @param T The type to return.
     * @return The cast object or [default].
     */
    @JvmName("castOrElse")
    public inline fun <T : Any> castOrElse(
        obj: Any?,
        kClass: KClass<T>,
        default: () -> T
    ): T = safeCast(obj, kClass) ?: default()

    /**
     * Casts the given [obj] to [T] or throws a [TypeCastException] if the cast fails.
     *
     * @param obj The object to cast.
     * @param kClass The class to cast to.
     * @param T The type to return.
     * @return The cast object.
     * @throws TypeCastException If the cast fails.
     */
    @JvmName("castOrThrow")
    @Throws(TypeCastException::class)
    public inline fun <T : Any> castOrThrow(
        obj: Any?,
        kClass: KClass<T>
    ): T = kClass.cast(obj)

    /**
     * Casts the given [obj] to [T] and triggers the [block] if successful.
     *
     * @param obj The object to cast.
     * @param kClass The class to cast to.
     * @param block The block to trigger if the cast is successful.
     * @param T The cast type.
     * @param R The type to return.
     * @return A nullable value of type [R].
     */
    @JvmName("withCast")
    public inline fun <T : Any, R> withCast(
        obj: Any?,
        kClass: KClass<T>,
        block: T.() -> R
    ): R? = safeCast(obj, kClass)?.block()
}
