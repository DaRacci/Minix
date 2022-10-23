package dev.racci.minix.api.exceptions

import dev.racci.minix.api.utils.kotlin.toSafeString

public class LevelConversionException(
    err: Throwable? = null,
    message: () -> Any? = { "Failed to convert level to string." }
) : RuntimeException(message.toSafeString(), err)
