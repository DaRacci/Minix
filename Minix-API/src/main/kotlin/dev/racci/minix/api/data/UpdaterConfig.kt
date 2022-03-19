package dev.racci.minix.api.data

import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.updater.providers.GitHubUpdateProvider
import dev.racci.minix.api.utils.data.Data
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

@ConfigSerializable
@MappedConfig(Minix::class, "updater.conf")
class UpdaterConfig {

    @Comment("Should the auto-updater be enabled?")
    var enabled: Boolean = true

    @Comment("The interval in minutes to check for updates")
    var interval: Duration = 1.hours

    @Comment("What folder should we use for old files and backups?")
    var updateFolder: String = "MinixUpdater"

    @Comment("Should the auto-updater announce it's progress?")
    var announceDownloadProgress: Boolean = true

    @Comment("Should we attempt to automatically download the dependencies too?")
    var downloadDependencies: Boolean = true

    var backups: BackupConfig = BackupConfig()

    @Comment("Auto-updater providers for plugins")
    var pluginUpdaters: MutableList<PluginUpdater> = mutableListOf(
        PluginUpdater(
            name = "Minix",
            providers = arrayOf(
                "Github" to GitHubUpdateProvider(
                    projectOwner = "DaRacci",
                    projectRepo = "Minix"
                )
            )
        )
    )

    @ConfigSerializable
    class BackupConfig(
        @Comment("Should we create a backup of the plugins folder before updating?")
        val enabled: Boolean = true,
        @Comment("What is the maximum size of a folder that is allowed to be backed up?")
        val maxSize: Data = Data.fromGigabytes(1)
    )
}
