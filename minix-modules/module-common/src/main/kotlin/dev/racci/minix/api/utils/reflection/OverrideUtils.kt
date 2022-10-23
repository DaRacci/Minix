package dev.racci.minix.api.utils.reflection

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

@API(status = API.Status.EXPERIMENTAL, since = "4.1.0")
public object OverrideUtils {

    /**
     * Does override function selector
     *
     * @param kClass The class to check if it overrides the function.
     * @param selector The function to select the function to check if it overrides.
     * @return If the function is overridden in the [kClass].
     */
    @JvmName("doesOverrideKFunctionSelector")
    public inline fun doesOverrideFunctionSelector(
        kClass: KClass<*>,
        selector: KFunction<*>.() -> Boolean
    ): Boolean = kClass.memberFunctions.find(selector) in kClass.declaredMemberFunctions

    @JvmName("doesOverrideKPropertySelector")
    public inline fun <T : Any> doesOverridePropertySelector(
        kClass: KClass<T>,
        selector: KProperty<*>.() -> Boolean
    ): Boolean = kClass.memberProperties.find(selector) in kClass.declaredMemberProperties

    @JvmName("doesOverrideKFunction")
    public inline fun doesOverrideFunction(
        kClass: KClass<*>,
        function: KFunction<*>
    ): Boolean = doesOverrideFunctionSelector(kClass) { this.name == function.name }

    @JvmName("doesOverrideKProperty")
    public inline fun <T : Any, R> doesOverrideProperty(
        kClass: KClass<T>,
        property: KProperty<R>
    ): Boolean = doesOverridePropertySelector(kClass) { this.name == property.name }

    @JvmName("ifOverridesKFunction")
    public inline fun <T : Any, C : KFunction<*>, R> ifOverridesFunction(
        kClass: KClass<T>,
        function: C,
        action: C.() -> R
    ): R? = doesOverrideFunctionInternal(kClass, function) { function.action() }

    @JvmName("ifOverridesKFunctionKCallable")
    public inline fun <T : Any, R> ifOverridesFunction(
        kClass: KClass<T>,
        function: KFunction0<R>
    ): R? = doesOverrideFunctionInternal(kClass, function, function::invoke)

    @JvmName("ifOverridesKSuspendFunction")
    public inline fun <T : Any, R> ifOverridesFunction(
        kClass: KClass<T>,
        function: KSuspendFunction0<R>
    ): R? = doesOverrideFunctionInternal(kClass, function) { runBlocking { function.invoke() } }

    @JvmName("ifOverridesKFunctionInstance")
    public inline fun <T : Any, R> ifOverridesFunction(
        instance: T,
        function: KFunction<*>,
        action: T.() -> R
    ): R? = doesOverrideFunctionInternal(instance::class, function) { instance.action() }

    @JvmName("ifOverridesKProperty")
    public inline fun <T : Any, C : KProperty<*>, R> ifOverridesProperty(
        kClass: KClass<T>,
        property: C,
        action: C.() -> R
    ): R? = doesOverridePropertyInternal(kClass, property) { property.action() }

    @JvmName("ifOverridesKPropertyInstance")
    public inline fun <T : Any, R> ifOverridesProperty(
        instance: T,
        property: KProperty<*>,
        action: T.() -> R
    ): R? = doesOverridePropertyInternal(instance::class, property) { instance.action() }

    public inline fun <R> callIfOverridesFunction(
        kClass: KClass<*>,
        function: KFunction0<R>
    ): R? = ifOverridesFunction(kClass, function)

    @JvmName("callIfOverridesFunctionSuspend")
    public inline fun <R> callIfOverridesFunction(
        kClass: KClass<*>,
        function: KSuspendFunction0<R>
    ): R? = ifOverridesFunction(kClass, function)

    @PublishedApi
    internal inline fun <R> doesOverrideFunctionInternal(
        kClass: KClass<*>,
        function: KFunction<*>,
        onSuccess: () -> R
    ): R? = when {
        doesOverrideFunction(kClass, function) -> onSuccess()
        else -> null
    }

    @PublishedApi
    internal inline fun <R> doesOverridePropertyInternal(
        kClass: KClass<*>,
        property: KProperty<*>,
        onSuccess: () -> R
    ): R? = when {
        doesOverrideProperty(kClass, property) -> onSuccess()
        else -> null
    }
}
