package dev.racci.minix.api.utils.reflection

import dev.racci.minix.api.utils.UtilObject
import org.apiguardian.api.API
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.safeCast

@[
    API(status = API.Status.EXPERIMENTAL, since = "4.1.0")
    Suppress("NOTHING_TO_INLINE")
]
object CastUtils : UtilObject by UtilObject {

    /**
     * Casts the given [obj] to [T] or returns null if the cast fails.
     *
     * @param obj The object to cast.
     * @param kClass The class to cast to.
     * @param T The type to return.
     * @return The cast object or null.
     */
    @JvmName("safeCast")
    inline fun <T : Any> safeCast(
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
    inline fun <T : Any> castOr(
        obj: Any?,
        kClass: KClass<T>,
        default: T
    ): T = this.safeCast(obj, kClass) ?: default

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
    inline fun <T : Any> castOrElse(
        obj: Any?,
        kClass: KClass<T>,
        default: () -> T
    ): T = this.safeCast(obj, kClass) ?: default()

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
    inline fun <T : Any> castOrThrow(
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
    inline fun <T : Any, R> withCast(
        obj: Any?,
        kClass: KClass<T>,
        block: T.() -> R
    ): R? = this.safeCast(obj, kClass)?.block()
}
