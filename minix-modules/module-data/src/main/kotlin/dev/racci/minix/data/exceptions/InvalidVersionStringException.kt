package dev.racci.minix.data.exceptions

import dev.racci.minix.data.Version

/**
 * This exception is thrown when the string representing a version is invalid.
 */
public class InvalidVersionStringException(
    string: String? = "The version string must be in the format: ${Version.versionStringRegex.pattern}"
) : IllegalArgumentException(string)
