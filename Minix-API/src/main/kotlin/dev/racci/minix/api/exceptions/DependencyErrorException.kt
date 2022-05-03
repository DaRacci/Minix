package dev.racci.minix.api.exceptions

/**
 * Thrown when a required dependency of an extension is not found.
 */
class DependencyErrorException(message: String) : Exception(message)
