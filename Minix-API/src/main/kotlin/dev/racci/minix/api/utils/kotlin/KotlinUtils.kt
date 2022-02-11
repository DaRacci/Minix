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

/**
 * Invokes the given block if the receiver overrides the supplied method.
 *
 * @param T The type of the receiver.
 * @param methodName The name of the method to check for.
 * @param block The block to invoke.
 * @return If the block was invoked.
 */
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

/**
 * Invokes the given block if the receiver is not null.
 *
 * @param T The type of the receiver.
 * @param block The block to invoke.
 * @return If the block was invoked.
 */
inline fun <T> T?.invokeIfNotNull(block: (T) -> Unit): Boolean = if (this != null) {
    block(this)
    true
} else false

/**
 * Invokes the given block if the receiver is null.
 *
 * @param T The type of the receiver.
 * @param block The block to invoke.
 * @return If the block was invoked
 */
inline fun <T> T?.invokeIfNull(block: () -> Unit): Boolean = if (this == null) {
    block()
    true
} else false

/**
 * Invokes the given block if the boolean is true.
 *
 * @param block The block to invoke.
 * @return If the block was invoked.
 */
inline fun Boolean?.ifTrue(block: () -> Unit): Boolean = if (this == true) {
    block()
    true
} else false

/**
 * Invokes the given block if the boolean is false.
 *
 * @param block The block to invoke.
 * @return If the block was invoked.
 */
inline fun Boolean?.ifFalse(block: () -> Unit): Boolean = if (this == false) {
    block()
    true
} else false

/**
 * Invokes the given block if the receiver is not empty.
 *
 * @param T The value type of the collection.
 * @param block The block to invoke.
 * @return The collection itself.
 */
inline fun <reified T> Collection<T>?.ifNotEmpty(block: (Collection<T>) -> Unit): Collection<T>? {
    if (this != null && this.isNotEmpty()) block(this)
    return this
}
