package dev.racci.minix.api.utils

import kotlin.reflect.KClass

class UnsafeUtil {

    private var dangerCallback: () -> Unit = { }
    private val exceptionHandlers = mutableMapOf<KClass<out Any>, Exception.() -> Unit>()

    fun danger(callback: () -> Unit) {
        this.dangerCallback = callback
    }

    fun catch(
        vararg exceptionTypes: KClass<out Any>,
        callback: Exception.() -> Unit = {},
    ) {
        exceptionTypes.forEach {
            exceptionHandlers[it] = callback
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun execute() {
        try {
            dangerCallback()
        } catch (ex: Exception) {
            exceptionHandlers
                .filter { (type, _) -> ex::class.simpleName == type.simpleName }
                .forEach { it.value(ex) }
        }
    }

    companion object {

        @Suppress("unused")
        fun Any.unsafe(callback: UnsafeUtil.() -> Unit) {
            UnsafeUtil().apply {
                callback()
                execute()
            }
        }
    }
}
