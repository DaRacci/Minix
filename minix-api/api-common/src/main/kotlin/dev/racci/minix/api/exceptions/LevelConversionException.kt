package dev.racci.minix.api.exceptions

class LevelConversionException(
    err: Throwable? = null,
    message: () -> Any? = { "Failed to convert level to string." }
    ) : RuntimeException(message.toSafeString(), err)
