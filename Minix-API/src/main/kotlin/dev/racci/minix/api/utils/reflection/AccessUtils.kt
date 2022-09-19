package dev.racci.minix.api.utils.reflection

import dev.racci.minix.api.utils.UtilObject
import org.apiguardian.api.API
import kotlin.reflect.KCallable
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KMutableProperty2
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
import kotlin.reflect.KProperty2
import kotlin.reflect.jvm.isAccessible

@[
    API(status = API.Status.EXPERIMENTAL, since = "4.1.0")
    Suppress("NOTHING_TO_INLINE")
]
object AccessUtils : UtilObject by UtilObject {

    @JvmName("accessWith")
    inline fun <R, A, C : KCallable<R>> accessWith(
        callable: C,
        action: C.() -> A
    ): A {
        val originalValue = callable.isAccessible
        callable.isAccessible = true
        val result = action(callable)
        callable.isAccessible = originalValue
        return result
    }

    @JvmName("accessGet0")
    inline fun <R> accessGet(
        property: KProperty0<R>
    ): R = this.accessWith(property) { this.get() }

    @JvmName("accessGet1")
    inline fun <T : Any, R> accessGet(
        property: KProperty1<T, R>,
        obj: T
    ): R = this.accessWith(property) { this.get(obj) }

    @JvmName("accessGet2")
    inline fun <T : Any, D, R> accessGet(
        property: KProperty2<T, D, R>,
        objT: T,
        objD: D
    ): R = this.accessWith(property) { this.get(objT, objD) }

    @JvmName("accessSet0")
    inline fun <R> accessSet(
        property: KMutableProperty0<R>,
        value: R
    ) = this.accessWith(property) { this.set(value) }

    @JvmName("accessSet1")
    inline fun <T : Any, R> accessSet(
        property: KMutableProperty1<T, R>,
        obj: T,
        value: R
    ) = this.accessWith(property) { this.set(obj, value) }

    @JvmName("accessSet2")
    inline fun <T : Any, D, R> accessSet(
        property: KMutableProperty2<T, D, R>,
        objT: T,
        objD: D,
        value: R
    ) = this.accessWith(property) { this.set(objT, objD, value) }
}
