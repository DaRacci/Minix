@file:Suppress("UNUSED", "TooGenericExceptionCaught")

package dev.racci.minix.api.utils.kotlin

import kotlin.reflect.KClass

inline fun <reified T : Throwable, reified U : Any> catch(
    err: (T) -> U,
    run: () -> U,
): U = try {
    run()
} catch (ex: Throwable) {
    if (ex is T) err(ex) else throw ex
}

inline fun <reified T : Throwable> catch(
    err: (T) -> Unit = { it.printStackTrace() },
    run: () -> Unit,
) = catch<T, Unit>(err, run)

inline fun <reified T : Throwable, reified U : Any> catch(
    default: U,
    run: () -> U,
): U = catch<T, U>({ default }, run)

inline fun <reified T : Throwable, R> catchAndReturn(
    err: (T) -> Unit = { it.printStackTrace() },
    run: () -> R,
): R? = try {
    run()
} catch (ex: Exception) {
    if (ex is T) err(ex) else throw ex
    null
}

infix fun KClass<*>.doesOverride(methodName: String): Boolean {
    return this.java.methods.find { it.name == methodName } in this.java.declaredMethods
}

inline fun <reified T : Any> T.invokeIfOverrides(
    methodName: String,
    block: (T) -> Unit
): Boolean {
    if (this::class.doesOverride(methodName)) {
        block(this)
        return true
    }
    return false
}

fun <T> T.not(other: T) = takeUnless { it == other }
fun <T> T.notIn(container: Iterable<T>) = takeUnless { it in container }

inline fun <T> T?.invokeIfNotNull(block: (T) -> Unit): T? {
    if (this != null) block(this)
    return this
}

inline fun <T> T?.invokeIfNull(block: () -> Unit): T? {
    if (this == null) block()
    return this
}

inline fun Boolean?.ifTrue(block: () -> Unit) {
    if (this == true) block()
}

inline fun Boolean?.ifFalse(block: () -> Unit) {
    if (this == false) block()
}

inline fun <reified T> Collection<T>.ifNotEmpty(block: (Collection<T>) -> Unit) {
    if (this.isNotEmpty()) block(this)
}
