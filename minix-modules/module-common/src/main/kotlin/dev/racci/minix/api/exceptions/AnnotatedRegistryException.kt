package dev.racci.minix.api.exceptions

public class AnnotatedRegistryException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : RuntimeException()
