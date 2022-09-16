package dev.racci.minix.api.utils.reflection

import dev.racci.minix.api.utils.UtilObject
import dev.racci.minix.api.utils.accessReturn
import org.apiguardian.api.API
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

@Suppress("NOTHING_TO_INLINE")
@API(status = API.Status.EXPERIMENTAL, since = "4.1.0")
object LazyUtil : UtilObject by UtilObject {

    @JvmName("isLazy0")
    inline fun isLazy(
        value: KProperty0<*>
    ): Boolean = value.accessReturn { getDelegate() is Lazy<*> }

    @JvmName("isLazy1")
    inline fun <T : Any> isLazy(
        value: KProperty1<T, *>,
        obj: T
    ): Boolean = value.accessReturn { getDelegate(obj) is Lazy<*> }

    @JvmName("isInitialised0")
    inline fun isInitialised(
        value: KProperty0<*>
    ): Boolean = value.accessReturn { getDelegate() is Lazy<*> && (getDelegate() as Lazy<*>).isInitialized() }

    @JvmName("isInitialised1")
    inline fun <T : Any> isInitialised(
        value: KProperty1<T, *>,
        obj: T
    ): Boolean = value.accessReturn { getDelegate(obj) is Lazy<*> && (getDelegate(obj) as Lazy<*>).isInitialized() }

    @JvmName("getOrNull0")
    inline fun <R> getOrNull(
        value: KProperty0<R>
    ): R? = if (isInitialised(value)) value.accessReturn { get() } else null

    @JvmName("getOrNull1")
    inline fun <T : Any, R> getOrNull(
        value: KProperty1<T, R>,
        obj: T
    ): R? = if (isInitialised(value, obj)) value.accessReturn { get(obj) } else null

    @JvmName("getOrThrow0")
    inline fun <R> getOrThrow(
        value: KProperty0<R>
    ): R = if (isInitialised(value)) value.accessReturn { get() } else error("Property is not initialized")

    @JvmName("getOrThrow1")
    inline fun <T : Any, R> getOrThrow(
        value: KProperty1<T, R>,
        obj: T
    ): R = if (isInitialised(value, obj)) value.accessReturn { get(obj) } else error("Property is not initialized")

    @JvmName("getOrElse0")
    inline fun <R> getOrElse(
        value: KProperty0<R>,
        defaultValue: () -> R
    ): R = if (isInitialised(value)) value.accessReturn { get() } else defaultValue()

    @JvmName("getOrElse1")
    inline fun <T : Any, R> getOrElse(
        value: KProperty1<T, R>,
        obj: T,
        defaultValue: () -> R
    ): R = if (isInitialised(value, obj)) value.accessReturn { get(obj) } else defaultValue()

    @JvmName("getOrDefault0")
    inline fun <R> getOrDefault(
        value: KProperty0<R>,
        default: R
    ): R = if (isInitialised(value)) value.accessReturn { get() } else default

    @JvmName("getOrDefault1")
    inline fun <T : Any, R> getOrDefault(
        value: KProperty1<T, R>,
        obj: T,
        default: R
    ): R = if (isInitialised(value, obj)) value.get(obj) else default

    @JvmName("ifInitialized0")
    inline fun <R> ifInitialised(
        value: KProperty0<R>,
        action: R.() -> Unit
    ) = if (isInitialised(value)) value.accessReturn { get() }.action() else Unit

    @JvmName("ifInitialized1")
    inline fun <T : Any, R> ifInitialised(
        value: KProperty1<T, R>,
        obj: T,
        action: R.() -> Unit
    ) = if (isInitialised(value, obj)) value.accessReturn { get(obj) }.action() else Unit
}
