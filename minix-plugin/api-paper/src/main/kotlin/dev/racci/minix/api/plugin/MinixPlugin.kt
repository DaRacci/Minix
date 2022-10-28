package dev.racci.minix.api.plugin

import dev.racci.minix.api.lifecycles.ComplexManagedLifecycle
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.logger.MinixLoggerFactory
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.data.Version
import org.apiguardian.api.API
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.get
import org.koin.core.component.getScopeId
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import java.nio.file.Path

/**
 * The main plugin class.
 */
public actual abstract class MinixPlugin :
    Qualifier,
    KoinScopeComponent,
    ComplexManagedLifecycle,
    WithPlugin<MinixPlugin>,
    PlatformPlugin,
    JavaPlugin() {

    actual final override val platformClassLoader: ClassLoader get() = this.classLoader

    actual final override val value: String get() = this.name

    actual final override val scope: Scope by lazy { getKoin().createScope(this.getScopeId(), named(this.value), this) }

    actual final override val plugin: MinixPlugin get() = this

    actual final override val logger: MinixLogger by MinixLoggerFactory

    actual final override val dataFolder: Path get() = this.getDataFolder().toPath()

    public actual override val version: Version by lazy { Version(this.description.version) }

    public actual val enabled: Boolean get() = this.isEnabled
    public actual val metrics: Metrics by lazy { Metrics(this) }

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

    /**
     * Called once when the plugin is loaded.
     * Check your platforms documentation for more information of when this is called.
     */
    actual override suspend fun handleLoad() { /* no-op */ }

    /**
     * Called once when the plugin is unloaded.
     * This plugin and all sub-applications should be unloaded from memory and should not be used again.
     */
    actual override suspend fun handleUnload() { /* no-op */ }

    /**
     * Called once after minix has completed its internal loading process.
     * Check your platforms documentation for more information of when this is called.
     */
    actual override suspend fun handlePostLoad() { /* no-op */ }
}
