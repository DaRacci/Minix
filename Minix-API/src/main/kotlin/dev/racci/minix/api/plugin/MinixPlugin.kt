package dev.racci.minix.api.plugin

import dev.racci.minix.api.annotations.MappedPlugin
import dev.racci.minix.api.annotations.MinixDsl
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.data.PluginUpdater
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.updater.Version
import dev.racci.minix.api.utils.safeCast
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import org.koin.core.component.get
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf

/**
 * The superclass replacing [JavaPlugin],
 * This class provides many more features and allows implementation into the
 * systems of Minix.
 */
abstract class MinixPlugin : JavaPlugin(), SusPlugin, Qualifier {
    @get:ApiStatus.Internal
    // Public to be used within the plugin service impl
    @Deprecated("Useless and uses unneeded resources.")
    @get:ScheduledForRemoval(inVersion = "4.0.0")
    val annotation by lazy { this::class.findAnnotation<MappedPlugin>() }

    final override val value = this.name
    final override val log get() = get<PluginService>()[this].log
    final override val bStatsId = this::class.findAnnotation<MappedPlugin>()?.bStatsId.takeIf { it != -1 }
    final override val metrics get() = get<PluginService>()[this].metrics
    final override val version by lazy { Version(description.version) }

    override val updater: PluginUpdater? = null

    override val bindToKClass = this::class.findAnnotation<MappedPlugin>()?.bindToKClass?.takeIf { it.isSubclassOf(MinixPlugin::class) }.safeCast<KClass<out MinixPlugin>>()

    @MinixInternal
    @ApiStatus.NonExtendable
    override fun onEnable() {
        get<PluginService>().startPlugin(this)
    }

    @MinixInternal
    @ApiStatus.NonExtendable
    override fun onDisable() {
        get<PluginService>().unloadPlugin(this)
    }

    @MinixInternal
    @ApiStatus.NonExtendable
    override fun onLoad() {
        get<PluginService>().loadPlugin(this)
    }

    @MinixDsl
    @Deprecated("Use the MappedPlugin annotation instead")
    @ScheduledForRemoval(inVersion = "4.0.0")
    protected suspend fun extensions(builder: suspend ExtensionsBuilder.() -> Unit) {
        builder(get<PluginService>()[this].extensionsBuilder)
    }

    @MinixDsl
    @Deprecated("Use the MappedPlugin annotation instead")
    @ScheduledForRemoval(inVersion = "4.0.0")
    inner class ExtensionsBuilder {

        @MinixDsl
        @Suppress("UNCHECKED_CAST")
        inline infix fun <reified P : MinixPlugin> add(noinline builder: ExtensionUnit<P>) {
            get<PluginService>()[this@MinixPlugin].extensions.add(builder as ExtensionUnit<MinixPlugin>)
        }
    }
}
