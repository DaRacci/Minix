package dev.racci.minix.core.data

import dev.racci.minix.api.annotations.MappedConfig
import dev.racci.minix.data.DataSize
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Setting
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@ConfigSerializable
@MappedConfig("updater.conf")
public class UpdaterConfig {

    @Comment("Should the auto-updater be enabled?")
    public var enabled: Boolean = true

    @Comment("The interval in minutes to check for updates")
    @Setting("interval")
    public var _interval: Long = 60

    public var interval: Duration
        get() = _interval.minutes
        set(value) { _interval = value.inWholeMinutes }

    @Comment("What folder should we use for old files and backups?")
    public var updateFolder: String = "MinixUpdater"

    @Comment("Verbose information about the update process")
    public var verbose: Boolean = false

    @Comment("Should the auto-updater announce it's progress?")
    public var announceDownloadProgress: Boolean = true

    @Comment("Should we attempt to automatically download the dependencies too?")
    public var downloadDependencies: Boolean = true

    public var backups: BackupConfig = BackupConfig()

    @Comment("Auto-updater providers for plugins")
    public var pluginUpdaters: List<PluginUpdater> = emptyList()

    @ConfigSerializable
    public class BackupConfig {

        @Comment("Should we create a backup of the plugins folder before updating?")
        public val enabled: Boolean = true

        @Comment("What is the maximum size of a folder that is allowed to be backed up?")
        public val maxSize: DataSize = DataSize.fromGigabytes(1)

        override fun toString(): String {
            return "BackupConfig(enabled=$enabled, maxSize=$maxSize)"
        }
    }

//    override fun toString(): String {
//        val builder = StringBuilder("UpdaterConfig:")
//        builder.append("enabled=$enabled:")
//        builder.append("interval=$interval:")
//        builder.append("updateFolder=$updateFolder:")
//        builder.append("announceDownloadProgress=$announceDownloadProgress:")
//        builder.append("downloadDependencies=$downloadDependencies:")
//        builder.append("backups=$backups:")
//        builder.append("pluginUpdaters=[${pluginUpdaters.joinToString(", ")}]:")
//        return builder.toString()
//    }
}
