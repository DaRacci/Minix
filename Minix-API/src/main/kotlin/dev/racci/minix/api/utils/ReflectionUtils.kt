@file:Suppress("UNUSED", "UNCHECKED_CAST")

package dev.racci.minix.api.utils

import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.extensions.reflection.accessSet
import dev.racci.minix.api.utils.kotlin.catchAndReturn
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import java.lang.reflect.Constructor
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
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
    vararg args: Any?
): T = constructor.newInstance(*args)

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new accessGet function", ReplaceWith("instance.accessGet<R>()", "dev.racci.minix.api.extensions.reflection.KCallable"))
fun <R> readInstanceProperty(
    instance: Any,
    propertyName: String,
    ignoreCase: Boolean = false
): R? = instance::class.members.firstOrNull {
    it.name.equals(propertyName, ignoreCase)
}?.also { it.isAccessible = true }.safeCast<KProperty1<Any, *>>()?.get(instance) as? R

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new safeCast function", ReplaceWith("this.safeCast<T>()", "dev.racci.minix.api.extensions.reflection.safeCast"))
fun <T> Any?.safeCast(type: Class<T>): T? = catchAndReturn<ClassCastException, T>({}) { type.cast(this) }

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new safeCast function", ReplaceWith("this.safeCast<T>()", "dev.racci.minix.api.extensions.reflection.safeCast"))
inline fun <reified T : Any> Any?.safeCast(type: KClass<T> = T::class): T? = this.safeCast(type.java)

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new castOrThrow function", ReplaceWith("this.castOrThrow<T>()", "import dev.racci.minix.api.extensions.reflection.castOrThrow"))
@Throws(ClassCastException::class)
fun <T> Any?.unsafeCast(): T = this as T

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new castOrThrow function", ReplaceWith("this.castOrThrow<T>()", "dev.racci.minix.api.extensions.reflection.castOrThrow"))
@Throws(ClassCastException::class)
fun <T> Any?.unsafeCast(type: Class<T>): T = type.cast(this)

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new castOrThrow function", ReplaceWith("this.castOrThrow<T>()", "dev.racci.minix.api.extensions.reflection.castOrThrow"))
inline fun <reified T : Any> Any?.unsafeCast(type: KClass<T> = T::class): T = this.unsafeCast(type.java)

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new withCast function", ReplaceWith("this.withCast<T>(block)", "dev.racci.minix.api.extensions.reflection.withCast"))
inline fun <reified T> Any?.tryCast(block: T.() -> Unit): Boolean = if (this is T) {
    block()
    true
} else false

/**
 * Read the properties of an instance through reflection,
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
        .associate { it.name to (replaceArgs[it] ?: it.accessGet(this@clone)) }
    // Safe to say this isn't null, because we checked it above
    primaryConstructor!!.callBy(consParams.associateWith { allValues[it.name] }).also { newInstance ->
        for (prop in mutableProperties) {
            prop.accessSet(newInstance, allValues[prop.name])
        }
    }
}

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new accessWith function", ReplaceWith("this.accessWith<R, T>(block)", "dev.racci.minix.api.extensions.reflection.KCallable"))
inline fun <T : KProperty1<*, *>, R> T.accessReturn(action: T.() -> R): R {
    val originalAccessLevel = isAccessible
    isAccessible = true
    val value = this.action()
    isAccessible = originalAccessLevel
    return value
}

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new accessWith function", ReplaceWith("this.accessWith<R, T>(block)", "dev.racci.minix.api.extensions.reflection.KCallable"))
inline fun <T : KProperty<*>, R> T.accessReturn(action: T.() -> R): R {
    val originalAccessLevel = isAccessible
    isAccessible = true
    val value = this.action()
    isAccessible = originalAccessLevel
    return value
}

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new accessWith function", ReplaceWith("this.accessWith<R, T>(block)", "dev.racci.minix.api.extensions.reflection.KCallable"))
inline fun <T : KProperty1<*, R>, R> T.accessWith(action: T.() -> Unit) {
    val originalAccessLevel = isAccessible
    isAccessible = true
    this.action()
    isAccessible = originalAccessLevel
}

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new accessWith function", ReplaceWith("this.accessWith<R, T>(block)", "dev.racci.minix.api.extensions.reflection.KCallable"))
inline fun <T : KProperty<R>, R> T.accessWith(action: T.() -> Unit) {
    val originalAccessLevel = isAccessible
    isAccessible = true
    this.action()
    isAccessible = originalAccessLevel
}
