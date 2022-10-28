package dev.racci.minix.core.logger

import dev.racci.minix.api.logger.MinixLogger
import io.github.slimjar.logging.ProcessLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

public object SlimJarProxy : ProcessLogger, KoinComponent {
    override fun log(
        message: String?,
        vararg args: Any?
    ): Unit = get<MinixLogger>().info { message?.format(*args) }

    override fun debug(
        message: String?,
        vararg args: Any?
    ): Unit = get<MinixLogger>().debug { message?.format(*args) }
}
