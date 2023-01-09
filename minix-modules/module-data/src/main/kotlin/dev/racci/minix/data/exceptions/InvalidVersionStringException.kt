package dev.racci.minix.data.exceptions

/**
 * This exception is thrown when the string representing a version is invalid.
 */
public class InvalidVersionStringException internal constructor(
    rawVersion: String
) : IllegalArgumentException("The provided version `$rawVersion` is invalid and could not be parsed into a [Version].")
