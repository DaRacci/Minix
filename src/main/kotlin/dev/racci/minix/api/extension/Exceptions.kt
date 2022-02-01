package dev.racci.minix.api.extension

import kotlin.reflect.KClass

/**
 * A base class for all custom exceptions in the instance framework.
 */
open class ExtensionsException : Exception()

/**
 * Thrown when an attempt to load an [Extension] fails.
 *
 * @property clazz The invalid [Extension] class.
 * @property reason Why this [Extension] is considered invalid.
 */
class InvalidExtensionException(
    val clazz: KClass<out Extension<*>>,
    val reason: String?
) : ExtensionsException() {

    override fun toString(): String {
        val formattedReason = if (reason != null) {
            " ($reason)"
        } else {
            ""
        }

        return "Invalid extension class: ${clazz.qualifiedName}$formattedReason"
    }
}
