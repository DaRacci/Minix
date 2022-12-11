package dev.racci.minix.api.plugin

import dev.racci.minix.api.lifecycles.ComplexManagedLifecycle
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.api.logger.MinixLoggerFactory
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.data.Version
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
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
    actual final override val version: Version by lazy { Version(this.description.version) }
    actual final override val dependencies: ImmutableSet<String> by lazy<ImmutableSet<String>>((this.description.depend + this.description.softDepend)::toImmutableSet)

    public actual val enabled: Boolean get() = this.isEnabled
    public actual val metrics: Metrics by lazy { Metrics(this) }

    @ApiStatus.NonExtendable
    @API(status = API.Status.INTERNAL)
    override fun onLoad() {
        get<PluginService>().loadPlugin(this)
    }

    @ApiStatus.NonExtendable
    @API(status = API.Status.INTERNAL)
    override fun onEnable() {
        get<PluginService>().enablePlugin(this)
    }

    @ApiStatus.NonExtendable
    @API(status = API.Status.INTERNAL)
    override fun onDisable() {
        get<PluginService>().disablePlugin(this)
        if (this.server.isStopping) get<PluginService>().unloadPlugin(this)
    }

    /**
     * Called once when the plugin is loaded by Bukkit.
     * The plugin is not enabled at this point and cannot register listeners and the like.
     */
    actual override suspend fun handleLoad() { /* no-op */ }

    /** Called once after minix has completed its internal loading process. */
    actual override suspend fun handlePostLoad() { /* no-op */ }

    /**
     * Called possibly multiple times within the lifecycle.
     * The plugin is considered enabled by bukkit and can now register listeners and the like.
     */
    actual override suspend fun handleEnable() { /* no-op */ }

    /** Called once after minix has completed its internal enabling process. */
    actual override suspend fun handlePostEnable() { /* no-op */ }

    /** Called possibly multiple times within the lifecycle while it is considered enabled. */
    actual override suspend fun handleReload() { /* no-op */ }

    /**
     * Called possibly multiple times within the lifecycle.
     * The plugin is considered disabled by bukkit and cannot register listeners and the like.
     */
    actual override suspend fun handleDisable() { /* no-op */ }

    /** Called once after minix has completed its internal disabling process. */
    actual override suspend fun handlePostDisable() { /* no-op */ }

    /**
     * Called once when the plugin is unloaded.
     * This plugin and all sub-applications should be unloaded from memory and should not be used again.
     */
    actual override suspend fun handleUnload() { /* no-op */ }

    /**
     * Called once after minix has completed its internal unloading process.
     * This plugin and all sub-applications should be unloaded from memory and should not be used again.
     */
    actual override suspend fun handlePostUnload() { /* no-op */ }
}
