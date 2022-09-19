package dev.racci.minix.api.utils.reflection

import dev.racci.minix.api.utils.UtilObject
import org.apiguardian.api.API
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

@[
    API(status = API.Status.EXPERIMENTAL, since = "4.1.0")
    Suppress("NOTHING_TO_INLINE")
]
object OverrideUtils : UtilObject by UtilObject {

    @JvmName("doesOverrideKFunctionSelector")
    inline fun <T : Any> doesOverrideFunctionSelector(
        kClass: KClass<T>,
        selector: KFunction<*>.() -> Boolean
    ): Boolean = kClass.memberFunctions.find(selector) in kClass.declaredMemberFunctions

    @JvmName("doesOverrideKPropertySelector")
    inline fun <T : Any> doesOverridePropertySelector(
        kClass: KClass<T>,
        selector: KProperty<*>.() -> Boolean
    ): Boolean = kClass.memberProperties.find(selector) in kClass.declaredMemberProperties

    @JvmName("doesOverrideKFunction")
    inline fun <T : Any, R> doesOverrideFunction(
        kClass: KClass<T>,
        function: KFunction<R>
    ): Boolean = this.doesOverrideFunctionSelector(kClass) { this == function }

    @JvmName("doesOverrideKProperty")
    inline fun <T : Any, R> doesOverrideProperty(
        kClass: KClass<T>,
        property: KProperty<R>
    ): Boolean = this.doesOverridePropertySelector(kClass) { this == property }

    @JvmName("ifOverridesKFunction")
    inline fun <T : Any, C : KFunction<T>, R> ifOverrides(
        kClass: KClass<T>,
        function: C,
        action: C.() -> R
    ): R? = when {
        this.doesOverrideFunction(kClass, function) -> function.action()
        else -> null
    }

    @JvmName("ifOverridesKProperty")
    inline fun <T : Any, C : KProperty<T>, R> ifOverrides(
        kClass: KClass<T>,
        property: C,
        action: C.() -> R
    ): R? = when {
        this.doesOverrideProperty(kClass, property) -> property.action()
        else -> null
    }
}
