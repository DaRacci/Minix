package dev.racci.minix.core.loggers

import arrow.core.None
import dev.racci.minix.api.plugin.logger.MinixLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE

object KoinProxy : Logger(), KoinComponent {
    override fun log(
        level: Level,
        msg: MESSAGE
    ) {
        when (level) {
            Level.DEBUG -> get<MinixLogger>().debug(msg = msg)
            Level.INFO -> get<MinixLogger>().info(msg = msg)
            Level.ERROR -> get<MinixLogger>().error(msg = msg)
            else -> None
        }
    }
}
