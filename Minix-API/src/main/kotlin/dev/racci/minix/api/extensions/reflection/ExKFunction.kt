package dev.racci.minix.api.extensions.reflection

import dev.racci.minix.api.utils.reflection.OverrideUtils
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction0
import kotlin.reflect.KSuspendFunction0

inline fun <reified T : Any> KFunction<*>.isOverriddenIn(): Boolean = OverrideUtils.doesOverrideFunction(T::class, this)

inline fun KFunction<*>.isOverriddenIn(
    instance: Any
): Boolean = OverrideUtils.doesOverrideFunction(instance::class, this)

inline fun <reified T : Any, R, C : KFunction<*>> C.ifOverriddenIn(
    action: C.() -> R
): R? = OverrideUtils.ifOverridesFunction(T::class, this, action)

inline fun <T : Any, R> KFunction<*>.ifOverriddenIn(
    instance: T,
    action: T.() -> R
): R? = OverrideUtils.ifOverridesFunction(instance, this, action)

inline fun <reified T : Any, R> KFunction0<R>.ifOverriddenIn(): R? = OverrideUtils.ifOverridesFunction(T::class, this)

@JvmName("ifOverriddenInSuspend")
inline fun <reified T : Any, R> KSuspendFunction0<R>.ifOverriddenIn(): R? = OverrideUtils.ifOverridesFunction(T::class, this)

@JvmName("callIfOverriddenKFunction0")
inline fun <T : Any, R> KFunction0<R>.callIfOverridden(
    kClass: KClass<T>
) = OverrideUtils.callIfOverridesFunction(kClass, this)

@JvmName("callIfOverriddenKSuspendFunction0")
inline fun <T : Any, R> KSuspendFunction0<R>.callIfOverridden(
    kClass: KClass<T>
) = OverrideUtils.callIfOverridesFunction(kClass, this)

@JvmName("callIfOverriddenKFunction0Reified")
inline fun <reified T : Any, R> KFunction0<R>.callIfOverridden() = this.callIfOverridden(T::class)

@JvmName("callIfOverriddenKSuspendFunction0Reified")
inline fun <reified T : Any, R> KSuspendFunction0<R>.callIfOverridden() = this.callIfOverridden(T::class)
