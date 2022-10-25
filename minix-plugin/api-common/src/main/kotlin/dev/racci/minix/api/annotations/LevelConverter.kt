package dev.racci.minix.api.annotations

import kotlin.reflect.KClass

/**
 * Marks a class as a [LevelConverter] for a specific platform.
 *
 * @param type The levels class representation.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class LevelConverter(
    public val type: KClass<out Any>
)
