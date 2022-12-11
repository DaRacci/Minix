package dev.racci.minix.api.plugin

import dev.racci.minix.api.lifecycles.ComplexManagedLifecycle
import dev.racci.minix.api.logger.MinixLogger
import dev.racci.minix.data.Version
import kotlinx.collections.immutable.ImmutableSet
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import java.nio.file.Path

/**
 * The main plugin class.
 */
public expect abstract class MinixPlugin :
    PlatformPlugin,
    WithPlugin<MinixPlugin>,
    Qualifier,
    ComplexManagedLifecycle {

    public final override val value: String

    public final override val scope: Scope

    public final override val plugin: MinixPlugin

    public final override val logger: MinixLogger

    public final override val platformClassLoader: ClassLoader

    public final override val dependencies: ImmutableSet<String>

    public final override val dataFolder: Path

    public final override val version: Version

    public val enabled: Boolean

    public val metrics: Metrics

    /**
     * Called possibly multiple times when the plugin is enabled.
     * Check your platforms documentation for more information of when this is called.
     */
    override suspend fun handleEnable()

    /**
     * Called possibly multiple times when the plugin is disabled.
     * Check your platforms documentation for more information of when this is called.
     */
    override suspend fun handleDisable()

    /**
     * Called once when the plugin is loaded.
     * Check your platforms documentation for more information of when this is called.
     */
    override suspend fun handleLoad()

    /**
     * Called possibly multiple times or never when the plugin is reloaded.
     * This is called the platforms reload command is called.
     */
    override suspend fun handleReload()

    /**
     * Called once when the plugin is unloaded.
     * This plugin and all sub-applications should be unloaded from memory and should not be used again.
     */
    override suspend fun handleUnload()

    /**
     * Called once after minix has completed its internal loading process.
     * Check your platforms documentation for more information of when this is called.
     */
    override suspend fun handlePostLoad()

    /**
     * Called possibly multiple times after the plugin is enabled.
     * This is called after Minix has completed its internal enabling process.
     * Check your platforms documentation for more information of when this is called.
     */
    override suspend fun handlePostEnable()

    /**
     * Called possibly multiple times after the plugin is disabled.
     * This is called after Minix has completed its internal disabling process.
     * Check your platforms documentation for more information of when this is called.
     */
    override suspend fun handlePostDisable()

    /** Called once after minix has completed its internal unloading process. */
    override suspend fun handlePostUnload()
}
