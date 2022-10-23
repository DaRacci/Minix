package dev.racci.minix.api.utils

import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass

private typealias Callback = suspend () -> Unit

// TODO -> Rework so this can be of actual use
public class UnsafeUtil {

    private var dangerCallback: Callback = { }
    private var finallyCallback: Callback? = null
    private val exceptionHandlers = mutableMapOf<KClass<out Any>, Exception.() -> Unit>()

    public infix fun danger(callback: Callback) {
        this.dangerCallback = callback
    }

    public fun catch(
        vararg exceptionTypes: KClass<out Any>,
        callback: Exception.() -> Unit
    ) {
        exceptionTypes.forEach {
            exceptionHandlers[it] = callback
        }
    }

    public infix fun finally(callback: Callback) {
        this.finallyCallback = callback
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun execute() {
        try {
            dangerCallback.invoke()
        } catch (ex: Exception) {
            exceptionHandlers
                .filter { (type, _) -> ex::class.simpleName == type.simpleName }
                .forEach { it.value(ex) }
        }
        finallyCallback?.invoke()
    }

    public companion object {

        public fun Any.unsafe(callback: UnsafeUtil.() -> Unit) {
            with(UnsafeUtil()) {
                callback()
                runBlocking { execute() }
            }
        }
    }
}
