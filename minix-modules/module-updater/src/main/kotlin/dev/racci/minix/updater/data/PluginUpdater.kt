package dev.racci.minix.updater.data

import dev.racci.minix.api.lifecycles.Loadable
import dev.racci.minix.api.services.UpdaterService
import dev.racci.minix.api.updater.UpdateMode
import dev.racci.minix.api.updater.UpdateResult
import dev.racci.minix.api.updater.providers.UpdateProvider
import dev.racci.minix.api.utils.Version
import dev.racci.minix.updater.UpdateMode
import dev.racci.minix.updater.UpdateResult
import dev.racci.minix.updater.UpdaterService
import dev.racci.minix.updater.providers.UpdateProvider
import kotlinx.datetime.Instant
import org.bukkit.plugin.Plugin
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Required

@ConfigSerializable
public class PluginUpdater {

    @Required
    @Comment("The name of the plugin")
    public lateinit var name: String

    @Comment("Which update type to use")
    public var updateMode: UpdateMode = UpdateMode.UPDATE

    @Required
    @Comment("What providers are supported by this plugin")
    public var providers: MutableList<UpdateProvider> = mutableListOf()

    @Comment("What release channels should be updated to")
    public var channels: Array<String> = arrayOf("release")

    @Comment("Should we ignore any folders, or files when backing up this plugin?")
    public var ignored: Array<String> = emptyArray()

    @Transient
    public var sentInfo: Boolean = false

    @Transient
    public var sentAvailable: Boolean = false

    @Transient
    public var failedAttempts: Int = 0
        set(value) {
            if (value > 4) {
                if (providers.size >= activeProvider + 1) {
                    activeProvider++
                    field = 0
                    sentInfo = false
                    sentAvailable = false
                } else {
                    UpdaterService.enabledUpdaters -= this
                    UpdaterService.disabledUpdaters += this
                }
            }
        }

    @Transient
    public var pluginInstance: Plugin? = null

    @Transient
    public var lastRun: Instant? = null

    @Transient
    public val localVersion: Loadable<Version> = Loadable.of {
        val instance = pluginInstance ?: error("Plugin instance is null")
        Version(instance.description.version)
    }

    public val localFile: String get() = pluginInstance!!::class.java.protectionDomain.codeSource.location.file

    @Transient
    public var result: UpdateResult? = null
        set(value) {
            if (value?.name?.startsWith("FAILED") == true) {
                failedAttempts++
            }
            field = value
        }

    @Transient
    public var activeProvider: Int = 0
    public val provider: UpdateProvider get() = providers[activeProvider]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PluginUpdater

        if (name != other.name) return false
        if (updateMode != other.updateMode) return false
        if (providers != other.providers) return false
        if (!ignored.contentEquals(other.ignored)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + updateMode.hashCode()
        result = 31 * result + providers.hashCode()
        result = 31 * result + ignored.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "PluginUpdater(name=$name:updateMode=$updateMode:providers=${providers.joinToString(":", "[", "]")}:ignored=${ignored.joinToString(":", "[", "]")})"
    }
}
