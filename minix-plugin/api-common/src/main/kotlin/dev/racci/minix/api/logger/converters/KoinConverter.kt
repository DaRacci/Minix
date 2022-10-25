package dev.racci.minix.api.logger.converters

import dev.racci.minix.api.annotations.LevelConverter
import dev.racci.minix.api.logger.LoggingLevel
import org.koin.core.logger.Level

@LevelConverter(Level::class)
internal class KoinConverter : LoggerConverter<Level> {
    override fun convert(level: LoggingLevel): Level {
        return when (level) {
            LoggingLevel.FATAL -> Level.ERROR
            LoggingLevel.ERROR -> Level.ERROR
            LoggingLevel.WARN -> Level.INFO
            LoggingLevel.INFO -> Level.INFO
            LoggingLevel.DEBUG -> Level.DEBUG
            LoggingLevel.TRACE -> Level.DEBUG
        }
    }

    override fun convert(level: Level): LoggingLevel {
        return when (level) {
            Level.DEBUG -> LoggingLevel.DEBUG
            Level.INFO -> LoggingLevel.INFO
            Level.ERROR -> LoggingLevel.ERROR
            Level.NONE -> LoggingLevel.FATAL // We don't support absolutely no logging
        }
    }
}
