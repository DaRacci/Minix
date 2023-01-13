package dev.racci.minix.core.logger

import arrow.core.None
import dev.racci.minix.api.logger.MinixLogger
import org.koin.core.component.KoinComponent
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE

internal object KoinProxy : Logger(), KoinComponent {
    internal lateinit var backingLogger: MinixLogger

    override fun display(
        level: Level,
        msg: MESSAGE
    ) {
        when (level) {
            Level.ERROR -> backingLogger.error(msg = { msg })
            Level.WARNING -> backingLogger.warn(msg = { msg })
            Level.INFO -> backingLogger.info(msg = { msg })
            Level.DEBUG -> backingLogger.debug(msg = { msg })
            else -> None
        }
    }
}
