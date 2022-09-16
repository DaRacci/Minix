package dev.racci.minix.api.utils

import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass

private typealias Callback = suspend () -> Unit

class UnsafeUtil {

    private var dangerCallback: Callback = { }
    private var finallyCallback: Callback? = null
    private val exceptionHandlers = mutableMapOf<KClass<out Any>, Exception.() -> Unit>()

    fun danger(callback: Callback) {
        this.dangerCallback = callback
    }

    fun catch(
        vararg exceptionTypes: KClass<out Any>,
        callback: Exception.() -> Unit
    ) {
        exceptionTypes.forEach {
            exceptionHandlers[it] = callback
        }
    }

    fun finally(callback: Callback) {
        this.finallyCallback = callback
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun execute() {
        try {
            dangerCallback()
        } catch (ex: Exception) {
            exceptionHandlers
                .filter { (type, _) -> ex::class.simpleName == type.simpleName }
                .forEach { it.value(ex) }
        }
        finallyCallback?.invoke()
    }

    companion object {

        @Suppress("unused")
        fun Any.unsafe(callback: UnsafeUtil.() -> Unit) {
            with(UnsafeUtil()) {
                callback()
                runBlocking { execute() }
            }
        }
    }
}
