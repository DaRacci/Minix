package dev.racci.minix.api.data

import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.services.UpdaterService
import dev.racci.minix.api.updater.UpdateMode
import dev.racci.minix.api.updater.UpdateResult
import dev.racci.minix.api.updater.Version
import dev.racci.minix.api.updater.providers.UpdateProvider
import dev.racci.minix.api.utils.getKoin
import kotlinx.datetime.Instant
import org.bukkit.plugin.Plugin
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment
import org.spongepowered.configurate.objectmapping.meta.Required

@ConfigSerializable
class PluginUpdater {

    @Required
    @Comment("The name of the plugin")
    lateinit var name: String

    @Comment("Which update type to use")
    var updateMode: UpdateMode = UpdateMode.UPDATE

    @Required
    @Comment("What providers are supported by this plugin")
    var providers: MutableList<UpdateProvider> = mutableListOf()

    @Comment("What release channels should be updated to")
    var channels: Array<String> = arrayOf("release")

    @Comment("Should we ignore any folders, or files when backing up this plugin?")
    var ignored: Array<String> = emptyArray()

    @Transient var sentInfo: Boolean = false

    @Transient var sentAvailable: Boolean = false

    @Transient var failedAttempts: Int = 0
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

    @Transient var pluginInstance: Plugin? = null

    @Transient var lastRun: Instant? = null

    @Transient private var _localVersion: Version? = null
    val localVersion: Version get() {
        if (_localVersion == null) {
            _localVersion = try {
                Version(pluginInstance!!.description.version)
            } catch (e: Version.InvalidVersionStringException) {
                getKoin().get<MinixLogger>().warn(e) { "The version string of ${pluginInstance!!.description.name} couldn't be matched." }
                Version.ERROR
            }
        }
        return _localVersion!!
    }
    val localFile: String get() = pluginInstance!!::class.java.protectionDomain.codeSource.location.file

    @Transient var result: UpdateResult? = null
        set(value) {
            if (value?.name?.startsWith("FAILED") == true) {
                failedAttempts++
            }
            field = value
        }

    @Transient var activeProvider: Int = 0
    val provider: UpdateProvider get() = providers[activeProvider]

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
