package dev.racci.minix.api.utils.reflection

import dev.racci.minix.api.utils.UtilObject
import kotlinx.coroutines.runBlocking
import org.apiguardian.api.API
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction0
import kotlin.reflect.KProperty
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

@[
    API(status = API.Status.EXPERIMENTAL, since = "4.1.0")
    Suppress("NOTHING_TO_INLINE")
]
object OverrideUtils : UtilObject by UtilObject {

    /**
     * Does override function selector
     *
     * @param kClass The class to check if it overrides the function.
     * @param selector The function to select the function to check if it overrides.
     * @return If the function is overridden in the [kClass].
     */
    @JvmName("doesOverrideKFunctionSelector")
    inline fun doesOverrideFunctionSelector(
        kClass: KClass<*>,
        selector: KFunction<*>.() -> Boolean
    ): Boolean = kClass.memberFunctions.find(selector) in kClass.declaredMemberFunctions

    @JvmName("doesOverrideKPropertySelector")
    inline fun <T : Any> doesOverridePropertySelector(
        kClass: KClass<T>,
        selector: KProperty<*>.() -> Boolean
    ): Boolean = kClass.memberProperties.find(selector) in kClass.declaredMemberProperties

    @JvmName("doesOverrideKFunction")
    inline fun doesOverrideFunction(
        kClass: KClass<*>,
        function: KFunction<*>
    ): Boolean = this.doesOverrideFunctionSelector(kClass) { this == function }

    @JvmName("doesOverrideKProperty")
    inline fun <T : Any, R> doesOverrideProperty(
        kClass: KClass<T>,
        property: KProperty<R>
    ): Boolean = this.doesOverridePropertySelector(kClass) { this == property }

    @JvmName("ifOverridesKFunction")
    inline fun <T : Any, C : KFunction<*>, R> ifOverridesFunction(
        kClass: KClass<T>,
        function: C,
        action: C.() -> R
    ): R? = this.doesOverrideFunctionInternal(kClass, function) { function.action() }

    @JvmName("ifOverridesKFunctionKCallable")
    inline fun <T : Any, R> ifOverridesFunction(
        kClass: KClass<T>,
        function: KFunction0<R>
    ): R? = this.doesOverrideFunctionInternal(kClass, function, function::invoke)

    @JvmName("ifOverridesKSuspendFunction")
    inline fun <T : Any, R> ifOverridesFunction(
        kClass: KClass<T>,
        function: KSuspendFunction0<R>
    ): R? = this.doesOverrideFunctionInternal(kClass, function) { runBlocking { function.invoke() } }

    @JvmName("ifOverridesKFunctionInstance")
    inline fun <T : Any, R> ifOverridesFunction(
        instance: T,
        function: KFunction<*>,
        action: T.() -> R
    ): R? = this.doesOverrideFunctionInternal(instance::class, function) { instance.action() }

    @JvmName("ifOverridesKProperty")
    inline fun <T : Any, C : KProperty<*>, R> ifOverridesProperty(
        kClass: KClass<T>,
        property: C,
        action: C.() -> R
    ): R? = this.doesOverridePropertyInternal(kClass, property) { property.action() }

    @JvmName("ifOverridesKPropertyInstance")
    inline fun <T : Any, R> ifOverridesProperty(
        instance: T,
        property: KProperty<*>,
        action: T.() -> R
    ): R? = this.doesOverridePropertyInternal(instance::class, property) { instance.action() }

    inline fun callIfOverridesFunction(
        kClass: KClass<*>,
        function: KFunction0<*>
    ) = this.ifOverridesFunction(kClass, function)

    @JvmName("callIfOverridesFunctionSuspend")
    inline fun callIfOverridesFunction(
        kClass: KClass<*>,
        function: KSuspendFunction0<*>
    ) = this.ifOverridesFunction(kClass, function)

    @PublishedApi
    internal inline fun <R> doesOverrideFunctionInternal(
        kClass: KClass<*>,
        function: KFunction<*>,
        onSuccess: () -> R
    ) = when {
        this.doesOverrideFunction(kClass, function) -> onSuccess()
        else -> null
    }

    @PublishedApi
    internal inline fun <R> doesOverridePropertyInternal(
        kClass: KClass<*>,
        property: KProperty<*>,
        onSuccess: () -> R
    ) = when {
        this.doesOverrideProperty(kClass, property) -> onSuccess()
        else -> null
    }
}
