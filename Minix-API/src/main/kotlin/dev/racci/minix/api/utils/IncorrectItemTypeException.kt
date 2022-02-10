package dev.racci.minix.api.utils

import org.jetbrains.annotations.ApiStatus

/**
 * Thrown when the item supplied to an item builder
 * is not of the correct type.
 */
@ApiStatus.Internal
@ApiStatus.AvailableSince("1.0.0")
class IncorrectItemTypeException(message: String) : Exception(message)
