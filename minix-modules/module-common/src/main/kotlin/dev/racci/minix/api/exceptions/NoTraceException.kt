package dev.racci.minix.api.exceptions

/** A [RuntimeException] without a stacktrace. */
public class NoTraceException(
    message: String? = null,
    cause: Throwable? = null
) : RuntimeException(message, cause) {
    override fun fillInStackTrace(): Throwable = this
}
