@file:Suppress("UNUSED", "UNCHECKED_CAST")

package dev.racci.minix.api.utils

import dev.racci.minix.api.utils.kotlin.catchAndReturn
import java.lang.reflect.Constructor
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

/**
 * Checks if a class exists.
 *
 * @param className The class to check.
 * @return True the class exists.
 */
fun exists(className: String): Boolean {
    return try {
        Class.forName(className); true
    } catch (e: ClassNotFoundException) { false }
}

/**
 * Constructs a new class instance.
 *
 * @param T The class to construct.
 * @param constructor The constructor to use.
 * @param args The arguments to pass to the constructor.
 * @return The constructed class instance.
 */
fun <T> classConstructor(
    constructor: Constructor<T>,
    vararg args: Any?,
): T = constructor.newInstance(*args)

/**
 * Reads and returns the value of a property from this instance.
 *
 * @param R The type of the property.
 * @param instance The instance to read from.
 * @param propertyName The name of the property to read.
 * @param ignoreCase Whether to ignore case when searching for the property.
 * @return The value of the property.
 */
fun <R> readInstanceProperty(
    instance: Any,
    propertyName: String,
    ignoreCase: Boolean = false,
): R? = instance::class.members.firstOrNull {
    it.name.equals(propertyName, ignoreCase)
}?.also { it.isAccessible = true }.safeCast<KProperty1<Any, *>>()?.get(instance) as? R

/**
 * Makes a safe cast of a property to a different type.
 *
 * @param T The type to cast to.
 * @return The casted property.
 */
fun <T> Any?.safeCast(): T? = this as? T

/**
 * Makes a safe cast using reflection
 *
 * @param T The type to cast to.
 * @param type The type to cast to.
 * @return The casted property.
 */
fun <T> Any?.safeCast(type: Class<T>): T? = catchAndReturn<ClassCastException, T>({}) { type.cast(this) }

/**
 * Makes a safe cast from an inline type to a different type.
 *
 * @param T The type to cast to.
 * @param type The type to cast to.
 * @return The casted property.
 */
inline fun <reified T : Any> Any?.safeCast(type: KClass<T> = T::class): T? = this.safeCast(type.java)

/**
 * Makes an unsafe cast of a property to a different type.
 *
 * @param T The type to cast to.
 * @return The casted property.
 * @throws ClassCastException If the property is not of the correct type.
 */
@Throws(ClassCastException::class)
fun <T> Any?.unsafeCast(): T = this as T

/**
 * Makes an unsafe cast of a property to a different type using reflection.
 *
 * @param T The type to cast to.
 * @param type The type to cast to.
 * @return The casted property.
 * @throws ClassCastException If the property is not of the correct type.
 */
@Throws(ClassCastException::class)
fun <T> Any?.unsafeCast(type: Class<T>): T = type.cast(this)

/**
 * Makes an unsafe cast from an inline type to a different type using reflection.
 *
 * @param T The type to cast to.
 * @param type The type to cast to.
 * @return The casted property.
 * @throws ClassCastException If the property is not of the correct type.
 */
inline fun <reified T : Any> Any?.unsafeCast(type: KClass<T> = T::class): T = this.unsafeCast(type.java)

/**
 * Makes a safe cast and invokes the function if successful.
 *
 * @param T The type to cast to.
 * @param block The function to invoke if successful.
 * @return If the cast was successful.
 */
inline fun <reified T> Any?.tryCast(block: T.() -> Unit): Boolean = if (this is T) {
    block()
    true
} else false

/**
 * Reads the properties of an instance through reflection,
 * and constructs a new instance of the same type.
 *
 * @param replaceArgs A map of property names to values to replace.
 * @return The new copy of the instance.
 */
fun <T : Any> T.clone(replaceArgs: Map<KProperty1<T, *>, Any> = emptyMap()): T = javaClass.kotlin.run {
    val consParams = primaryConstructor?.parameters ?: error("No primary constructor found")
    val mutableProperties = memberProperties.filterIsInstance<KMutableProperty1<T, Any?>>()
    val allValues = memberProperties
        .filter { it in mutableProperties || it.name in consParams.map(KParameter::name) }
        .associate { it.name to (replaceArgs[it] ?: it.get(this@clone)) }
    // Safe to say this isn't null, because we checked it above
    primaryConstructor!!.callBy(consParams.associateWith { allValues[it.name] }).also { newInstance ->
        for (prop in mutableProperties) {
            prop.set(newInstance, allValues[prop.name])
        }
    }
}
