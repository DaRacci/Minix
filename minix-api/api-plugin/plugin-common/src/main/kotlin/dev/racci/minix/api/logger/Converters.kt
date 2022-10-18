package dev.racci.minix.api.logger

import org.koin.core.logger.Level

internal object Converters {

    internal object KoinConverter : LoggerConverter<Level> {
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
}
