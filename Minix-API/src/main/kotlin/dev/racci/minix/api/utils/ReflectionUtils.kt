@file:Suppress("UNUSED", "UNCHECKED_CAST")

package dev.racci.minix.api.utils

import dev.racci.minix.api.utils.kotlin.catchAndReturn
import java.lang.reflect.Constructor
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * Checks if a class exists.
 *
 * @param className the class to check
 * @return weather the class exists or not
 */
fun exists(className: String): Boolean {
    catchAndReturn<ClassNotFoundException, Boolean>({ return false }) {
        Class.forName(className)
        true
    }
    return false
}

fun <T> classConstructor(
    constructor: Constructor<T>,
    vararg args: Any?,
): T = constructor.newInstance(*args)

fun <R> readInstanceProperty(
    instance: Any,
    propertyName: String,
    ignoreCase: Boolean = false,
): R {
    val property = instance::class.members
        // don't cast here to <Any, R>, it would succeed silently
        .first { it.name.equals(propertyName, ignoreCase) } as KProperty1<Any, *>
    // force an invalid cast exception if incorrect type here
    return property.get(instance) as R
}

inline fun <reified T> Any?.tryCast(block: T.() -> Unit): Boolean = if (this is T) {
    block()
    true
} else false

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
