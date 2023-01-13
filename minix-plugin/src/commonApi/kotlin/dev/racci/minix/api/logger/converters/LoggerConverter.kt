package dev.racci.minix.api.logger.converters

import dev.racci.minix.api.exceptions.LevelConversionException
import dev.racci.minix.api.logger.LoggingLevel
import org.apiguardian.api.API

/**
 * A converter for converting a [LoggingLevel] to a logger level.
 *
 * @param T The type of the logger level.
 */
@API(status = API.Status.EXPERIMENTAL, since = "5.0.0")
public interface LoggerConverter<T : Any> {

    /**
     * Convert a [LoggingLevel] to an instance of [T]
     *
     * @param level The level to convert from.
     * @return The converted level of [T].
     * @throws LevelConversionException If the conversion fails.
     */
    @Throws(LevelConversionException::class)
    public fun convert(level: LoggingLevel): T

    /**
     * Convert an instance of [T] to the corresponding [LoggingLevel].
     *
     * @param level The level to convert from.
     * @return The converted level of [LoggingLevel].
     * @throws LevelConversionException If the conversion fails.
     */
    @Throws(LevelConversionException::class)
    public fun convert(level: T): LoggingLevel
}
