package dev.racci.minix.api.utils

import kotlin.reflect.KProperty

abstract class Closeable<T : Any> {

    protected var value: T? = null

    operator fun getValue(ref: Any, property: KProperty<*>): T = get()

    fun get(): T {
        if (value == null)
            try {
                value = create()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        return value!!
    }

    abstract fun create(): T

    open fun close() {
        value = null
    }
}
