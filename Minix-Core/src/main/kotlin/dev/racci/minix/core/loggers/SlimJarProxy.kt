package dev.racci.minix.core.loggers

import dev.racci.minix.api.plugin.logger.MinixLogger
import io.github.slimjar.logging.ProcessLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object SlimJarProxy : ProcessLogger, KoinComponent {
    override fun log(
        message: String?,
        vararg args: Any?
    ) = get<MinixLogger>().info { message?.format(*args) }

    override fun debug(
        message: String?,
        vararg args: Any?
    ) = get<MinixLogger>().debug { message?.format(*args) }
}
