package dev.racci.minix.core.data

import dev.racci.minix.api.annotations.Named
import dev.racci.minix.api.data.MinixConfig
import dev.racci.minix.core.plugin.Minix
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.util.UUID

@ConfigSerializable
@Named("config")
public class MinixActualConfig : MinixConfig<Minix>(true) {

    @Setting("server-uuid")
    @Comment("This server unique uuid (Please don't change this)")
    public var serverUUID: UUID = UUID.randomUUID()

    @Comment(
        """
        Should sentry be enabled?
        This will send errors relating to Minix and my plugins,
        I do not collect / sell any personal information,
        however some information like your server version, fork, and ip will be
        sent for identification and error tracking.
        """
    )
    public var sentryEnabled: Boolean = true
}
