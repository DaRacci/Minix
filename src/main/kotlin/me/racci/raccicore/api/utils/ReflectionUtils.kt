@file:Suppress("UNUSED", "UNCHECKED_CAST")
package me.racci.raccicore.api.utils

import java.lang.reflect.Constructor
import kotlin.reflect.KProperty1

/**
 * Checks if a class exists
 *
 * @param className the class to check
 * @return weather the class exists or not
 */
fun exists(className: String): Boolean {
    catch<ClassNotFoundException>({return false}) {
        Class.forName(className)
        return true
    }
    return false
}

fun <T> classConstructor(constructor: Constructor<T>, vararg args: Any?): T {
    return constructor.newInstance(*args)
}

fun <R> readInstanceProperty(
    instance: Any,
    propertyName: String,
    ignoreCase: Boolean = false,
): R {
    val property = instance::class.members
            // don't cast here to <Any, R>, it would succeed silently
            .first{it.name.equals(propertyName, ignoreCase)} as KProperty1<Any, *>
    // force an invalid cast exception if incorrect type here
    return property.get(instance) as R
}

inline fun <reified T> Any?.tryCast(block: T.() -> Unit): Boolean {
    return if (this is T) {
        block()
        true
    } else false
}