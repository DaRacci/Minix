package dev.racci.minix.api.utils.reflection

import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.extensions.reflection.accessWith
import org.apiguardian.api.API
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

@API(status = API.Status.EXPERIMENTAL, since = "4.1.0")
public object LazyUtils {

    @JvmName("isLazy0")
    public suspend inline fun isLazy(
        value: KProperty0<*>
    ): Boolean = value.accessWith { getDelegate() is Lazy<*> }

    @JvmName("isLazy1")
    public suspend inline fun <T : Any> isLazy(
        value: KProperty1<T, *>,
        obj: T
    ): Boolean = value.accessWith { getDelegate(obj) is Lazy<*> }

    @JvmName("isInitialised0")
    public suspend inline fun isInitialised(
        value: KProperty0<*>
    ): Boolean = value.accessWith { getDelegate() is Lazy<*> && (getDelegate() as Lazy<*>).isInitialized() }

    @JvmName("isInitialised1")
    public suspend inline fun <T : Any> isInitialised(
        value: KProperty1<T, *>,
        obj: T
    ): Boolean = value.accessWith { getDelegate(obj) is Lazy<*> && (getDelegate(obj) as Lazy<*>).isInitialized() }

    @JvmName("getOrNull0")
    public suspend inline fun <R> getOrNull(
        value: KProperty0<R>
    ): R? = if (isInitialised(value)) value.accessGet() else null

    @JvmName("getOrNull1")
    public suspend inline fun <T : Any, R> getOrNull(
        value: KProperty1<T, R>,
        obj: T
    ): R? = if (isInitialised(value, obj)) value.accessGet(obj) else null

    @JvmName("getOrThrow0")
    public suspend inline fun <R> getOrThrow(
        value: KProperty0<R>
    ): R = if (isInitialised(value)) value.accessGet() else error("Property is not initialized")

    @JvmName("getOrThrow1")
    public suspend inline fun <T : Any, R> getOrThrow(
        value: KProperty1<T, R>,
        obj: T
    ): R = if (isInitialised(value, obj)) value.accessGet(obj) else error("Property is not initialized")

    @JvmName("getOrElse0")
    public suspend inline fun <R> getOrElse(
        value: KProperty0<R>,
        defaultValue: () -> R
    ): R = if (isInitialised(value)) value.accessGet() else defaultValue()

    @JvmName("getOrElse1")
    public suspend inline fun <T : Any, R> getOrElse(
        value: KProperty1<T, R>,
        obj: T,
        defaultValue: () -> R
    ): R = if (isInitialised(value, obj)) value.accessGet(obj) else defaultValue()

    @JvmName("getOrDefault0")
    public suspend inline fun <R> getOrDefault(
        value: KProperty0<R>,
        default: R
    ): R = if (isInitialised(value)) value.accessGet() else default

    @JvmName("getOrDefault1")
    public suspend inline fun <T : Any, R> getOrDefault(
        value: KProperty1<T, R>,
        obj: T,
        default: R
    ): R = if (isInitialised(value, obj)) value.get(obj) else default

    @JvmName("ifInitialized0")
    public suspend inline fun <R> ifInitialised(
        value: KProperty0<R>,
        action: R.() -> Unit
    ): Unit = if (isInitialised(value)) value.accessGet().action() else Unit

    @JvmName("ifInitialized1")
    public suspend inline fun <T : Any, R> ifInitialised(
        value: KProperty1<T, R>,
        obj: T,
        action: R.() -> Unit
    ): Unit = if (isInitialised(value, obj)) value.accessGet(obj).action() else Unit
}
