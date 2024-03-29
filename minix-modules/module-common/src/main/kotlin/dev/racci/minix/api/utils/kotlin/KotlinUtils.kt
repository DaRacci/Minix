@file:Suppress("UNUSED", "TooGenericExceptionCaught")

package dev.racci.minix.api.utils.kotlin

import arrow.core.Either
import dev.racci.minix.api.exceptions.LevelConversionException
import dev.racci.minix.api.extensions.reflection.castOrThrow
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public inline fun <reified T : Throwable, A> Either.Companion.catch(
    block: () -> A
): Either<T, A> = catch {
    block()
}.tapLeft { err ->
    if (err !is T) throw err
}.castOrThrow()

public inline fun <reified T : Throwable, reified U : Any> catch(
    err: (T) -> U,
    run: () -> U
): U = try {
    run()
} catch (ex: Throwable) {
    if (ex is T) err(ex) else throw ex
}

public inline fun <reified T : Throwable> catch(
    err: (T) -> Unit = { it.printStackTrace() },
    run: () -> Unit
): Unit = catch<T, Unit>(err, run)

public inline fun <reified T : Throwable, reified U : Any> catch(
    default: U,
    run: () -> U
): U = catch<T, U>({ default }, run)

public inline fun <reified T : Throwable, R> catchAndReturn(
    err: (T) -> Unit = { it.printStackTrace() },
    run: () -> R
): R? = try {
    run()
} catch (ex: Exception) {
    if (ex is T) err(ex) else throw ex
    null
}

/**
 * Run a try catch and if there is an exception return false
 *
 * @param T The Throwable type
 * @param errorCallback What to run if there was an error, defaults to false
 * @param run The block to catch on
 * @return True if there was no exception, false if there was
 */
public inline fun <reified T : Throwable> booleanCatch(
    errorCallback: (T) -> Boolean = { true },
    run: () -> Any?
): Boolean = try {
    run()
    true
} catch (t: Throwable) {
    if (t is T) errorCallback(t) else throw t
}

public infix fun KClass<*>.doesOverride(functionName: String): Boolean {
    val function = this.functions.find { it.name == functionName }
    if (function != null) return this.doesOverride(function)

    val property = this.memberProperties.find { it.name == functionName }
    if (property != null) return this.doesOverride(property)

    return false
}

public infix fun KClass<*>.doesOverride(function: KFunction<*>): Boolean {
    return this.functions.find { it == function } in declaredFunctions
}

public infix fun KClass<*>.doesOverride(property: KProperty1<*, *>): Boolean {
    return this.memberProperties.find { it == property } in memberProperties
}

/**
 * Invokes the given block if the receiver overrides the supplied method.
 *
 * @param T The type of the receiver.
 * @param methodName The name of the method to check for.
 * @param block The block to invoke.
 * @return If the block was invoked.
 */
public inline fun <reified T : Any> T.invokeIfOverrides(
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
 * Calls the supplied action if the receiver is not null.
 *
 * @param T The type of the receiver.
 * @param action The action to invoke if the receiver is not null.
 * @return true if the receiver is not null, false otherwise.
 */
@OptIn(ExperimentalContracts::class)
public inline fun <T> T?.ifNotNull(action: T.() -> Unit): Boolean {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        returns(true) implies (this@ifNotNull != null)
        returns(false) implies (this@ifNotNull == null)
    }

    return if (this != null) {
        action(this)
        true
    } else false
}

/**
 * Calls the supplied action if the receiver is null.
 *
 * @param T The type of the receiver.
 * @param action The action to invoke if the receiver is null.
 * @return true if the receiver is null, false otherwise.
 */
@OptIn(ExperimentalContracts::class)
public inline fun <T : Any> T?.ifNull(action: () -> Unit): Boolean {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        returns(true) implies (this@ifNull == null)
    }

    return if (this == null) {
        action()
        true
    } else false
}

/**
 * Calls the supplied action if the receiver is true.
 *
 * @param action The action to invoke if the receiver is true.
 * @return true if the receiver is true, false otherwise.
 */
@OptIn(ExperimentalContracts::class)
public inline fun Boolean?.ifTrue(action: () -> Unit): Boolean {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        returns(true) implies (this@ifTrue != null)
    }

    return if (this == true) {
        action()
        true
    } else false
}

/**
 * Calls the supplied action if the receiver is false.
 *
 * @param action The action to invoke if the receiver is false.
 * @return true if the receiver is false, false otherwise.
 */
@OptIn(ExperimentalContracts::class)
public inline fun Boolean?.ifFalse(action: () -> Unit): Boolean {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
        returns(true) implies (this@ifFalse != null)
    }

    return if (this == false) {
        action()
        true
    } else false
}

/**
 * Invokes the given block if the receiver is not empty.
 *
 * @param T The value type of the collection.
 * @param block The block to invoke.
 * @return The collection itself.
 */
@OptIn(ExperimentalContracts::class)
public inline fun <reified T> Collection<T>?.ifNotEmpty(block: Collection<T>.() -> Unit): Collection<T>? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        returns(true) implies (this@ifNotEmpty != null)
    }

    if (!this.isNullOrEmpty()) block(this)
    return this
}

public infix fun <F, S, T> Pair<F, S>.to(other: T): Triple<F, S, T> = Triple(first, second, other)

/** Gets the parent kClass of a companionObject else null. */
public val <T : Any> KClass<T>.companionParent: KClass<out Any>? get() = if (isCompanion) java.declaringClass.kotlin else null

/** Get an enum instance from the ordinal value. */
@Throws(LevelConversionException::class)
public inline fun <reified E : Enum<*>> Enum.Companion.fromOrdinal(
    ordinal: Int,
    orElse: (Int) -> E = { throw LevelConversionException { "Couldn't convert $ordinal to an enum of ${E::class.simpleName}" } }
): E = E::class.java.enumConstants.getOrElse(ordinal, orElse)

/** Converts an invokable to a string safely. */
public fun (() -> Any?)?.toSafeString(): String {
    return try {
        this?.invoke().toString()
    } catch (e: Exception) {
        "String invocation failed: $e"
    }
}
