package dev.racci.minix.api.annotations

/**
 * Dsl marker for Minix dsls.
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class MinixDsl
