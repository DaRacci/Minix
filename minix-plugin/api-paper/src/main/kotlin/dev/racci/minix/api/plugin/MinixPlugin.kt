package dev.racci.minix.api.plugin

import dev.racci.minix.api.lifecycles.ComplexManagedLifecycle
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.services.PluginService
import org.apiguardian.api.API
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import org.koin.core.component.get
import org.koin.core.qualifier.Qualifier
import java.nio.file.Path

/**
 * The main plugin class.
 */
public actual abstract class MinixPlugin : Qualifier, ComplexManagedLifecycle, WithPlugin<MinixPlugin>, JavaPlugin() {
    actual final override val value: String get() = this.name
    actual final override val plugin: MinixPlugin get() = this
    actual final override val logger: MinixLogger get() = PluginService[this].logger
    actual final override val dataFolder: Path get() = this.getDataFolder().toPath()
    public actual val log: MinixLogger get() = logger

    @ApiStatus.NonExtendable
    @API(status = API.Status.INTERNAL)
    override fun onEnable() {
        get<PluginService>().startPlugin(this)
    }

    @ApiStatus.NonExtendable
    @API(status = API.Status.INTERNAL)
    override fun onDisable() {
        get<PluginService>().unloadPlugin(this)
    }

    @ApiStatus.NonExtendable
    @API(status = API.Status.INTERNAL)
    override fun onLoad() {
        get<PluginService>().loadPlugin(this)
    }
}
