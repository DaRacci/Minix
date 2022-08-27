package dev.racci.minix.core.data

import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.api.plugin.Minix
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.util.UUID

@ConfigSerializable
@MappedConfig(Minix::class, "config.conf")
class MinixConfig : MinixConfig<Minix>(true) {

    @Setting("server-uuid")
    @Comment("This server unique uuid (Please don't change this)")
    var serverUUID: UUID = UUID.randomUUID()

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
