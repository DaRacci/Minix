package dev.racci.minix.api.exceptions

/**
 * Thrown when a required dependency of an extension is not found.
 */
public class DependencyErrorException(message: String) : Exception(message)
