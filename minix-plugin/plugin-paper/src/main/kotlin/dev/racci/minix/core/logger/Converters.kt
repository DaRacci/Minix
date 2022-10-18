package dev.racci.minix.core.logger

import dev.racci.minix.api.exceptions.LevelConversionException
import dev.racci.minix.api.logger.LoggerConverter
import dev.racci.minix.api.logger.LoggingLevel
import org.apache.logging.log4j.spi.StandardLevel
import java.util.logging.Level

internal object Converters {

    // TODO: Is there a math equation for this?
    object Java : LoggerConverter<Level> {
        override fun convert(level: LoggingLevel): Level {
            return when (level) {
                LoggingLevel.FATAL -> Level.OFF
                LoggingLevel.ERROR -> Level.SEVERE
                LoggingLevel.WARN -> Level.WARNING
                LoggingLevel.INFO -> Level.INFO
                LoggingLevel.DEBUG -> Level.CONFIG
                LoggingLevel.TRACE -> Level.FINEST
            }
        }

        override fun convert(level: Level): LoggingLevel {
            return when (level) {
                Level.ALL, Level.FINEST -> LoggingLevel.TRACE
                Level.FINER, Level.FINE, Level.CONFIG -> LoggingLevel.DEBUG
                Level.INFO -> LoggingLevel.INFO
                Level.WARNING -> LoggingLevel.WARN
                Level.SEVERE -> LoggingLevel.ERROR
                Level.OFF -> LoggingLevel.FATAL
                else -> throw LevelConversionException { "Unknown level: $level" }
            }
        }
    }

    object SlF4 : LoggerConverter<org.slf4j.event.Level> {
        private const val SLF4_OFFSET: Int = 1

        override fun convert(level: LoggingLevel): org.slf4j.event.Level {
            return Enum.fromOrdinal(level.ordinal - SLF4_OFFSET)
        }

        override fun convert(level: org.slf4j.event.Level): LoggingLevel {
            return Enum.fromOrdinal(level.ordinal + SLF4_OFFSET)
        }
    }

    object Log4J : LoggerConverter<org.apache.logging.log4j.Level> {
        override fun convert(level: LoggingLevel): org.apache.logging.log4j.Level {
            val ordinal = ((level.ordinal + 1) * 100)
            val standardLevel = StandardLevel.getStandardLevel(ordinal).name
            return org.apache.logging.log4j.Level.getLevel(standardLevel)
        }

        override fun convert(level: org.apache.logging.log4j.Level): LoggingLevel {
            val ordinal = ((level.intLevel() / 100 % 10) - 1).coerceAtLeast(0)
            return Enum.fromOrdinal(ordinal)
        }
    }
}
