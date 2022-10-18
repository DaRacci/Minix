package dev.racci.minix.api.exceptions

import org.jetbrains.annotations.ApiStatus

/**
 * Thrown when the item supplied to an item builder
 * is not of the correct type.
 */
@ApiStatus.Internal
class IncorrectItemTypeException(message: String) : Exception(message)
