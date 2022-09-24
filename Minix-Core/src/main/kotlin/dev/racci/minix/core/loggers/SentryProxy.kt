package dev.racci.minix.core.loggers

import dev.racci.minix.api.plugin.logger.MinixLogger
import io.sentry.ILogger
import io.sentry.SentryLevel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class SentryProxy : ILogger, KoinComponent {

    override fun log(
        level: SentryLevel,
        message: String,
        vararg args: Any?
    ) = this.log(
        level = level,
        message = message,
        throwable = null,
        args = args
    )

    override fun log(
        level: SentryLevel,
        message: String,
        throwable: Throwable?
    ) = this.log(
        level = level,
        message = message,
        throwable = throwable,
        args = emptyArray()
    )

    override fun log(
        level: SentryLevel,
        throwable: Throwable?,
        message: String,
        vararg args: Any?
    ) {
        when (level) {
            SentryLevel.DEBUG -> get<MinixLogger>().debug(throwable, null) { message.format(*args) }
            SentryLevel.INFO -> get<MinixLogger>().info(throwable, null) { message.format(*args) }
            SentryLevel.WARNING -> get<MinixLogger>().warn(throwable, null) { message.format(*args) }
            SentryLevel.ERROR -> get<MinixLogger>().error(throwable, null) { message.format(*args) }
            SentryLevel.FATAL -> get<MinixLogger>().fatal(throwable, null) { message.format(*args) }
        }
    }

    override fun isEnabled(level: SentryLevel?) = false
}
