package dev.racci.minix.core.logger

import dev.racci.minix.api.logger.MinixLogger
import io.github.slimjar.logging.ProcessLogger
import org.koin.core.component.KoinComponent

public class SlimJarProxy(private val backer: MinixLogger) : ProcessLogger, KoinComponent {
    override fun log(
        message: String?,
        vararg args: Any?
    ): Unit = backer.info { message?.format(*args) }

    override fun debug(
        message: String?,
        vararg args: Any?
    ): Unit = backer.debug { message?.format(*args) }
}
