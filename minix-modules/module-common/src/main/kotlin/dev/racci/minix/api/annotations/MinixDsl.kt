package dev.racci.minix.api.annotations

/**
 * Dsl marker for Minix dsls.
 */
@DslMarker
@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
public annotation class MinixDsl
