package dev.racci.minix.core.loggers

import arrow.core.None
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.utils.getKoin
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE

class KoinProxy : Logger() {
    override fun log(
        level: Level,
        msg: MESSAGE
    ) {
        when (level) {
            Level.DEBUG -> getKoin().get<MinixLogger>().debug(msg = msg)
            Level.INFO -> getKoin().get<MinixLogger>().info(msg = msg)
            Level.ERROR -> getKoin().get<MinixLogger>().error(msg = msg)
            else -> None
        }
    }
}
