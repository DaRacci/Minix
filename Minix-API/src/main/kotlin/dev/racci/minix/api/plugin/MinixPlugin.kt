package dev.racci.minix.api.plugin

import dev.racci.minix.api.annotations.MappedPlugin
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.data.PluginUpdater
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.updater.Version
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import org.koin.core.component.get
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.full.findAnnotation

/**
 * The superclass replacing [JavaPlugin],
 * This class provides many more features and allows implementation into the
 * systems of Minix.
 */
abstract class MinixPlugin : JavaPlugin(), SusPlugin, Qualifier {
    final override val value = this.name
    final override val log get() = get<PluginService>()[this].log
    final override val bStatsId = this::class.findAnnotation<MappedPlugin>()?.bStatsId.takeIf { it != -1 }
    final override val metrics get() = get<PluginService>()[this].metrics
    final override val version by lazy { Version(description.version) }

    override val updater: PluginUpdater? = null

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
}
