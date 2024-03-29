package dev.racci.minix.api.extensions.reflection

import dev.racci.minix.api.utils.reflection.AccessUtils
import dev.racci.minix.api.utils.reflection.OverrideUtils
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction0
import kotlin.reflect.KSuspendFunction0

public inline fun <reified T : Any> KFunction<*>.isOverriddenIn(): Boolean = OverrideUtils.doesOverrideFunction(T::class, this)

public inline fun KFunction<*>.isOverriddenIn(
    instance: Any
): Boolean = OverrideUtils.doesOverrideFunction(instance::class, this)

public inline fun <reified T : Any, R, C : KFunction<*>> C.ifOverriddenIn(
    action: C.() -> R
): R? = OverrideUtils.ifOverridesFunction(T::class, this, action)

public inline fun <T : Any, R> KFunction<*>.ifOverriddenIn(
    instance: T,
    action: T.() -> R
): R? = OverrideUtils.ifOverridesFunction(instance, this, action)

public inline fun <reified T : Any, R> KFunction0<R>.ifOverriddenIn(): R? = OverrideUtils.ifOverridesFunction(T::class, this)

@JvmName("ifOverriddenInSuspend")
public inline fun <reified T : Any, R> KSuspendFunction0<R>.ifOverriddenIn(): R? = OverrideUtils.ifOverridesFunction(T::class, this)

@JvmName("callIfOverriddenKFunction0")
public inline fun <T : Any, R> KFunction0<R>.callIfOverridden(
    kClass: KClass<T>
): R? = OverrideUtils.callIfOverridesFunction(kClass, this)

@JvmName("callIfOverriddenKSuspendFunction0")
public inline fun <T : Any, R> KSuspendFunction0<R>.callIfOverridden(
    kClass: KClass<T>
): R? = OverrideUtils.callIfOverridesFunction(kClass, this)

@JvmName("callIfOverriddenKFunction0Reified")
public inline fun <reified T : Any, R> KFunction0<R>.callIfOverridden(): Any? = this.callIfOverridden(T::class)

@JvmName("callIfOverriddenKSuspendFunction0Reified")
public inline fun <reified T : Any, R> KSuspendFunction0<R>.callIfOverridden(): Any? = this.callIfOverridden(T::class)

/** @see AccessUtils.accessInvoke */
public suspend inline fun <R> KFunction<R>.accessInvoke(vararg args: Any?): R = AccessUtils.accessInvoke(this, *args)

/** @see AccessUtils.accessInvoke */
public suspend inline fun <R> KFunction0<R>.accessInvoke(): R = AccessUtils.accessInvoke(this)
