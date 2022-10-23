package dev.racci.minix.api.utils

import dev.racci.minix.api.utils.kotlin.catchAndReturn
import dev.racci.minix.api.utils.reflection.ReflectionUtils
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import java.lang.reflect.Constructor
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.isAccessible

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use ReflectionUtils.classForName instead.", ReplaceWith("ReflectionUtils.classForName(className).isValid", "dev.racci.minix.api.utils.reflection.ReflectionUtils"))
public fun exists(className: String): Boolean {
    return try {
        Class.forName(className); true
    } catch (e: ClassNotFoundException) { false }
}

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Unnecessary.", ReplaceWith("constructor.newInstance(*args)"))
public fun <T> classConstructor(
    constructor: Constructor<T>,
    vararg args: Any?
): T = constructor.newInstance(*args)

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new accessGet function", ReplaceWith("instance.accessGet<R>()", "dev.racci.minix.api.extensions.reflection.KCallable"))
public fun <R> readInstanceProperty(
    instance: Any,
    propertyName: String,
    ignoreCase: Boolean = false
): R? = instance::class.members.firstOrNull {
    it.name.equals(propertyName, ignoreCase)
}?.also { it.isAccessible = true }.safeCast<KProperty1<Any, *>>()?.get(instance) as? R

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new safeCast function", ReplaceWith("this.safeCast<T>()", "dev.racci.minix.api.extensions.reflection.safeCast"))
public fun <T> Any?.safeCast(type: Class<T>): T? = catchAndReturn<ClassCastException, T>({}) { type.cast(this) }

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new safeCast function", ReplaceWith("this.safeCast<T>()", "dev.racci.minix.api.extensions.reflection.safeCast"))
public inline fun <reified T : Any> Any?.safeCast(type: KClass<T> = T::class): T? = this.safeCast(type.java)

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new castOrThrow function", ReplaceWith("this.castOrThrow<T>()", "import dev.racci.minix.api.extensions.reflection.castOrThrow"))
@Throws(ClassCastException::class)
public fun <T> Any?.unsafeCast(): T = this as T

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new castOrThrow function", ReplaceWith("this.castOrThrow<T>()", "dev.racci.minix.api.extensions.reflection.castOrThrow"))
@Throws(ClassCastException::class)
public fun <T> Any?.unsafeCast(type: Class<T>): T = type.cast(this)

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new castOrThrow function", ReplaceWith("this.castOrThrow<T>()", "dev.racci.minix.api.extensions.reflection.castOrThrow"))
public inline fun <reified T : Any> Any?.unsafeCast(type: KClass<T> = T::class): T = this.unsafeCast(type.java)

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new withCast function", ReplaceWith("this.withCast<T>(block)", "dev.racci.minix.api.extensions.reflection.withCast"))
public inline fun <reified T> Any?.tryCast(block: T.() -> Unit): Boolean = if (this is T) {
    block()
    true
} else false

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new withCast function", ReplaceWith("this.clone(replaceArgs)", "dev.racci.minix.api.extensions.reflection.ReflectionUtils"))
public suspend fun <T : Any> T.clone(replaceArgs: Map<KProperty1<T, *>, Any> = emptyMap()): T = ReflectionUtils.clone(this, replaceArgs)

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new accessWith function", ReplaceWith("this.accessWith<T, R>(action)", "dev.racci.minix.api.extensions.reflection.accessWith"))
public inline fun <T : KProperty1<*, *>, R> T.accessReturn(action: T.() -> R): R {
    val originalAccessLevel = isAccessible
    isAccessible = true
    val value = this.action()
    isAccessible = originalAccessLevel
    return value
}

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new accessWith function", ReplaceWith("this.accessWith<T, R>(action)", "dev.racci.minix.api.extensions.reflection.accessWith"))
public inline fun <T : KProperty<*>, R> T.accessReturn(action: T.() -> R): R {
    val originalAccessLevel = isAccessible
    isAccessible = true
    val value = this.action()
    isAccessible = originalAccessLevel
    return value
}

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new accessWith function", ReplaceWith("this.accessWith<T, R>(action)", "dev.racci.minix.api.extensions.reflection.accessWith"))
public inline fun <T : KProperty1<*, R>, R> T.accessWith(action: T.() -> Unit) {
    val originalAccessLevel = isAccessible
    isAccessible = true
    this.action()
    isAccessible = originalAccessLevel
}

@ScheduledForRemoval(inVersion = "4.5.0")
@Deprecated("Use new accessWith function", ReplaceWith("this.accessWith<R, T>(action)", "dev.racci.minix.api.extensions.reflection.accessWith"))
public inline fun <T : KProperty<R>, R> T.accessWith(action: T.() -> Unit) {
    val originalAccessLevel = isAccessible
    isAccessible = true
    this.action()
    isAccessible = originalAccessLevel
}
