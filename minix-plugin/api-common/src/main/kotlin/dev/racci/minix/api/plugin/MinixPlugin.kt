package dev.racci.minix.api.plugin

import dev.racci.minix.api.lifecycles.ComplexManagedLifecycle
import dev.racci.minix.api.logger.MinixLogger
import org.jetbrains.annotations.ApiStatus
import org.koin.core.qualifier.Qualifier
import java.nio.file.Path

/**
 * The main plugin class.
 */
public expect abstract class MinixPlugin : WithPlugin<MinixPlugin>, Qualifier, ComplexManagedLifecycle {

    public final override val value: String

    public final override val plugin: MinixPlugin

    public final override val logger: MinixLogger

    public final override val dataFolder: Path

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
    override suspend fun handleCreate()

    /**
     * Called possibly multiple times or never when the plugin is reloaded.
     * This is called the platforms reload command is called.
     */
    override suspend fun handleReload()

    /**
     * Called once when the plugin is unloaded.
     * This plugin and all sub-applications should be unloaded from memory and should not be used again.
     */
    override suspend fun handleDispose()

    /**
     * Called once after minix has completed its internal loading process.
     * Check your platforms documentation for more information of when this is called.
     */
    override suspend fun handlePostCreate()

    /**
     * Called possibly multiple times after the plugin is enabled.
     * This is called after Minix has completed its internal enabling process.
     * Check your platforms documentation for more information of when this is called.
     */
    override suspend fun handlePostEnable()

    @get:ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @Deprecated("Use logger instead", ReplaceWith("logger"))
    public val log: MinixLogger
}
