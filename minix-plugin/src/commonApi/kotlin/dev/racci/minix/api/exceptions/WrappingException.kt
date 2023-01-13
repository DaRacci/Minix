package dev.racci.minix.api.exceptions

import kotlin.reflect.KClass

/**
 * An exception that was caused while wrapping an object.
 *
 * @param err The error that occurred.
 * @param received The object that was received.
 * @param expected The expected type('s) of the object.
 */
public class WrappingException internal constructor(
    err: Throwable? = null,
    received: Any?,
    vararg expected: KClass<*>
) : RuntimeException("Expected${if (expected.isEmpty()) "" else " one of"} ${expected.joinToString { it.simpleName.toString() }} but received ${received?.let { it::class.simpleName ?: "null" } ?: "null"}", err)
