package dev.racci.minix.api.exceptions

public class MinixConfigException : IllegalStateException {
    public constructor(message: String) : super(message)
    public constructor(message: String, cause: Throwable) : super(message, cause)
    public constructor(cause: Throwable) : super(cause)
}
