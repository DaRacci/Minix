package dev.racci.minix.api.utils.reflection

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.apiguardian.api.API
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction0
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KMutableProperty2
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
import kotlin.reflect.KProperty2
import kotlin.reflect.jvm.isAccessible

@API(status = API.Status.EXPERIMENTAL, since = "4.1.0")
public object AccessUtils {
    @PublishedApi
    internal val lockMap: ConcurrentHashMap<Any, Mutex> = ConcurrentHashMap<Any, Mutex>()

    @JvmName("accessWith")
    public suspend inline fun <A, C : KCallable<*>> accessWith(
        callable: C,
        action: C.() -> A
    ): A {
        val lock = lockMap.getOrPut(callable) { Mutex() }
        val result = lock.withLock {
            val originValue = callable.isAccessible
            callable.isAccessible = true
            val result = callable.action()
            callable.isAccessible = originValue

            result
        }

        return result
    }

    @JvmName("accessGet0")
    public suspend inline fun <R> accessGet(
        property: KProperty0<R>
    ): R = accessWith(property) { this.get() }

    @JvmName("accessGet1")
    public suspend inline fun <T : Any, R> accessGet(
        property: KProperty1<T, R>,
        obj: T
    ): R = accessWith(property) { this.get(obj) }

    @JvmName("accessGet2")
    public suspend inline fun <T : Any, D, R> accessGet(
        property: KProperty2<T, D, R>,
        objT: T,
        objD: D
    ): R = accessWith(property) { this.get(objT, objD) }

    @JvmName("accessSet0")
    public suspend inline fun <R> accessSet(
        property: KMutableProperty0<R>,
        value: R
    ): Unit = accessWith(property) { this.set(value) }

    @JvmName("accessSet1")
    public suspend inline fun <T : Any, R> accessSet(
        property: KMutableProperty1<T, R>,
        obj: T,
        value: R
    ): Unit = accessWith(property) { this.set(obj, value) }

    @JvmName("accessSet2")
    public suspend inline fun <T : Any, D, R> accessSet(
        property: KMutableProperty2<T, D, R>,
        objT: T,
        objD: D,
        value: R
    ): Unit = accessWith(property) { this.set(objT, objD, value) }

    @JvmName("accessInvoke")
    public suspend inline fun <R> accessInvoke(
        function: KFunction<R>,
        vararg args: Any?
    ): R = accessWith(function) { this.call(*args) }

    @JvmName("accessInvoke0")
    public suspend inline fun <R> accessInvoke(
        function: KFunction0<R>
    ): R = accessWith(function) { this.call() }
}
