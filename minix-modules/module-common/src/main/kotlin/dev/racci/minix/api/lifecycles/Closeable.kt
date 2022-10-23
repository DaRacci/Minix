package dev.racci.minix.api.lifecycles

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import org.apiguardian.api.API
import kotlin.reflect.KProperty

/**
 * An atomic reference value, which has methods that define its creation and disposal / being closed.
 * This class should be used as an anonymous object such as:
 * ```
 * val closeable = object : Closeable<Type>() {
 *    override fun create(): T {
 *       // create and return T
 *    }
 *
 *    override fun onClose() {
 *       // dispose of T
 *    }
 *```
 *
 * @param T The type of the value.
 */
@API(status = API.Status.STABLE, since = "2.7.1")
public abstract class Closeable<T : Any> {

    protected val value: AtomicRef<T?> = atomic(null)
    private var closed = false

    public operator fun getValue(ref: Any, property: KProperty<*>): T = get()

    public fun get(): T {
        if (value.value == null || closed) {
            try {
                value.lazySet(create())
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
        return value.value!!
    }

    /** Create a new instance of the value or just update it then return itself if needed. */
    public abstract fun create(): T

    /**
     * Sets value to false and [closed] to true.
     * If overridden, should set closed to true and call [onClose] if used.
     *
     * @return Returns the value of [closed] before closing or null.
     */
    public fun close(): T? {
        onClose()
        closed = true
        return value.getAndSet(null)
    }

    /** Called when [close] is called but before the value is disposed of. */
    public open fun onClose(): Unit = Unit
}
