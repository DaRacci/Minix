package dev.racci.minix.core.logger

import dev.racci.minix.api.exceptions.LevelConversionException
import dev.racci.minix.api.logger.LoggingLevel
import dev.racci.minix.api.logger.converters.LoggerConverter
import dev.racci.minix.api.utils.kotlin.fromOrdinal
import org.apache.logging.log4j.spi.StandardLevel
import java.util.logging.Level as JLevel
import org.apache.logging.log4j.Level as Log4JLevel
import org.slf4j.event.Level as Slf4Level

@Suppress("unused") // Used Via Reflection
internal object Converters {

    // TODO: Is there a math equation for this?
    object Java : LoggerConverter<JLevel> {
        override fun convert(level: LoggingLevel): JLevel {
            return when (level) {
                LoggingLevel.FATAL -> JLevel.OFF
                LoggingLevel.ERROR -> JLevel.SEVERE
                LoggingLevel.WARN -> JLevel.WARNING
                LoggingLevel.INFO -> JLevel.INFO
                LoggingLevel.DEBUG -> JLevel.CONFIG
                LoggingLevel.TRACE -> JLevel.FINEST
            }
        }

        override fun convert(level: JLevel): LoggingLevel {
            return when (level) {
                JLevel.ALL, JLevel.FINEST -> LoggingLevel.TRACE
                JLevel.FINER, JLevel.FINE, JLevel.CONFIG -> LoggingLevel.DEBUG
                JLevel.INFO -> LoggingLevel.INFO
                JLevel.WARNING -> LoggingLevel.WARN
                JLevel.SEVERE -> LoggingLevel.ERROR
                JLevel.OFF -> LoggingLevel.FATAL
                else -> throw LevelConversionException { "Unknown level: $level" }
            }
        }
    }

    object SlF4 : LoggerConverter<Slf4Level> {
        private const val SLF4_OFFSET: Int = 1

        override fun convert(level: LoggingLevel): Slf4Level {
            return Enum.fromOrdinal(level.ordinal - SLF4_OFFSET)
        }

        override fun convert(level: Slf4Level): LoggingLevel {
            return Enum.fromOrdinal(level.ordinal + SLF4_OFFSET)
        }
    }

    object Log4J : LoggerConverter<Log4JLevel> {
        override fun convert(level: LoggingLevel): Log4JLevel {
            val ordinal = ((level.ordinal + 1) * 100)
            val standardLevel = StandardLevel.getStandardLevel(ordinal).name
            return Log4JLevel.getLevel(standardLevel)
        }

        override fun convert(level: Log4JLevel): LoggingLevel {
            val ordinal = ((level.intLevel() / 100 % 10) - 1).coerceAtLeast(0)
            return Enum.fromOrdinal(ordinal)
        }
    }
}
