package dev.racci.minix.api.annotations

/**
 * Place this annotation on an event function to force it to invoke async.
 */
@Target(AnnotationTarget.FUNCTION)
public annotation class RunAsync
