package dev.racci.minix.api.annotations

import arrow.core.Either
import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/** An abstraction for specifying the name of what is being annotated. */
@Experimental
@AvailableSince("5.0.0")
@MinixReflectiveAPI
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Named(val name: String) {
    public companion object {
        /**
         * Gets the formatted name of the callers class.
         * If this class is annotated with [Named], then the provided name will be used,
         * otherwise the class' simple name will be used.
         *
         * The name is formatted with [withFormat].
         *
         * @return A lazy evaluation of the formatted name of the class.
         */
        public fun delegate(): Lazy<String> {
            // TODO: This might get the superclass instead, need to test.
            val caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).callerClass
            return lazy { of(caller.kotlin) }
        }

        /**
         * Gets the formatted name of the given class.
         * If this class is annotated with [Named], then the provided name will be used,
         * otherwise the class' simple name will be used.
         *
         * The name is formatted with [withFormat].
         *
         * @param T The reified type of the class.
         * @return The formatted name of the class.
         */
        public inline fun <reified T : Any> of(): String = of(T::class)

        /**
         * Gets the formatted name of the given class.
         * If this class is annotated with [Named], then the provided name will be used,
         * otherwise the class' simple name will be used.
         *
         * The name is formatted with [withFormat].
         *
         * @param kClass The class to get the name of.
         * @return The formatted name of the class.
         */
        public fun of(kClass: KClass<*>): String = Either.fromNullable(kClass.findAnnotation<Named>()).fold(
            { kClass.simpleName ?: error("Could not find name for class `${kClass.qualifiedName}`.") },
            { it.name }
        ).let(::withFormat)

        /**
         * Formats the given string,
         * Replacing all spaces with '-' and at all instances of an uppercase char,
         * with a lowercase char and a '-' before it.
         *
         * @param string The string to format.
         * @return The formatted string.
         */
        public fun withFormat(string: String): String = buildString {
            string.forEach { char ->
                when (char) {
                    in 'A'..'Z' -> append('-').append(char.lowercaseChar())
                    ' ' -> append('-')
                    else -> append(char)
                }
            }
        }
    }
}
