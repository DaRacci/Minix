package dev.racci.minix.core.data

import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.logger.MinixLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.util.UUID

@ConfigSerializable
@MappedConfig(Minix::class, "config.conf")
class MinixConfig : KoinComponent {

    @Setting("server-uuid")
    @Comment("This server unique uuid (Please don't change this)")
    var serverUUID: UUID = UUID.randomUUID()

    @Comment("What log level should be used? [FATAL, ERROR, WARNING, INFO, DEBUG, TRACE]")
    var loggingLevel: String = MinixLogger.LoggingLevel.INFO.name
        set(value) {
            val uppercase = value.uppercase()
            val logger = get<MinixLogger>()
            val level = MinixLogger.LoggingLevel.values().find { it.name == uppercase } ?: return logger.warn { "Invalid logging level $value" }
            field = value
            getKoin().get<Minix>().log.setLevel(level)
        }

    @Comment(
        """
        Should sentry be enabled?
        This will send errors relating to Minix and my plugins,
        I do not collect / sell any personal information,
        however some information like your server version, fork, and ip will be
        sent for identification and error tracking.
        """
    )
    var sentryEnabled: Boolean = true
}
