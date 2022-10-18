package dev.racci.minix.api.lifecycles

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import kotlin.reflect.KProperty

/**
 * An atomic reference value which has methods which define its creation and disposal / being closed.
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
abstract class Closeable<T : Any> {

    protected val value: AtomicRef<T?> = atomic(null)
    private var closed = false

    operator fun getValue(ref: Any, property: KProperty<*>): T = get()

    fun get(): T {
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
    abstract fun create(): T

    /**
     * Sets value to false and [closed] to true.
     * If overridden, should set closed to true and call [onClose] if used.
     *
     * @return Returns the value of [closed] before closing or null.
     */
    open fun close(): T? {
        onClose()
        closed = true
        return value.getAndSet(null)
    }

    /** Called when [close] is called but before the value is disposed of. */
    open fun onClose() = Unit
}
