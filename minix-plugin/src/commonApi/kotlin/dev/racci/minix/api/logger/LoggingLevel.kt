package dev.racci.minix.api.logger
import dev.racci.minix.api.exceptions.LevelConversionException
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.logger.converters.LoggerConverter
import org.apiguardian.api.API
import kotlin.jvm.Throws
import kotlin.reflect.KClass

/**
 * A Utility for using levels between common loggers.
 * Levels are sorted by high level to low-level logs.
 * This allows for easy filtering using [Int] comparisons.
 */
@API(status = API.Status.STABLE, since = "3.2.0")
public enum class LoggingLevel {
    FATAL,
    ERROR,
    WARN,
    INFO,
    DEBUG,
    TRACE;

    public val level: Int
        get() = ordinal * 2

    /** @see [LoggerConverter.convert] */
    @Throws(LevelConversionException::class)
    public inline fun <reified T : Any> convert(): T {
        return getConverter<T>().convert(this)
    }

    public companion object {
        @PublishedApi
        internal lateinit var CONVERTERS: MutableMap<KClass<out Any>, LoggerConverter<out Any>>

        @PublishedApi
        @Throws(LevelConversionException::class)
        internal inline fun <reified T : Any> getConverter(): LoggerConverter<T> = CONVERTERS.getOrElse(T::class) {
            throw LevelConversionException { "No converter found for ${T::class.simpleName}" }
        }.castOrThrow() // Shouldn't be possible to throw as the type should be the same as the key.

        /** @see [LoggerConverter.convert] */
        public inline fun <reified T : Any> convert(level: T): LoggingLevel = getConverter<T>().convert(level)
    }
}
