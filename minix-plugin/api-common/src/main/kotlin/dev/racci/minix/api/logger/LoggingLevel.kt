package dev.racci.minix.api.logger

/**
 * A Utility for using levels between common loggers.
 * [LoggingLevel]'s are sorted by high level to low-level logs.
 * This allows for easy filtering using [Int] comparisons.
 */
import dev.racci.minix.api.exceptions.LevelConversionException
import dev.racci.minix.api.logger.converters.LoggerConverter
import org.apiguardian.api.API
import kotlin.jvm.Throws
import kotlin.reflect.KClass

@API(status = API.Status.STABLE, since = "3.2.0")
public enum class LoggingLevel(public val level: Int) {
    FATAL(0),
    ERROR(2),
    WARN(4),
    INFO(6),
    DEBUG(8),
    TRACE(10);

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
        internal inline fun <reified T : Any> getConverter(): LoggerConverter<T> {
            return CONVERTERS[T::class] as? LoggerConverter<T> ?: throw LevelConversionException {
                "No converter found for ${T::class.simpleName}"
            }
        }

        /** @see [LoggerConverter.convert] */
        public inline fun <reified T : Any> convert(level: T): LoggingLevel {
            return getConverter<T>().convert(level)
        }
    }
}
