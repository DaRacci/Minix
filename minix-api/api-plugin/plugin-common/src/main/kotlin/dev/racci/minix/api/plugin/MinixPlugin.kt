package dev.racci.minix.api.plugin

import org.koin.core.qualifier.Qualifier

/**
 * The main plugin class.
 */
public expect abstract class MinixPlugin : Qualifier {

    public final override val value: String

    /**
     * Called possibly multiple times when the plugin is enabled.
     * Check your platforms documentation for more information of when this is called.
     */
    protected suspend fun handleEnabled()

    /**
     * Called possibly multiple times when the plugin is disabled.
     * Check your platforms documentation for more information of when this is called.
     */
    protected suspend fun handleDisable()

    /**
     * Called once when the plugin is loaded.
     * Check your platforms documentation for more information of when this is called.
     */
    protected suspend fun handleLoad()

    /**
     * Called possibly multiple times or never when the plugin is reloaded.
     * This is called the platforms reload command is called.
     */
    protected suspend fun handleReload()

    /**
     * Called once when the plugin is unloaded.
     * This plugin and all sub-applications should be unloaded from memory and should not be used again.
     */
    protected suspend fun handleUnload()

    /**
     * Called once after minix has completed its internal loading process.
     * Check your platforms documentation for more information of when this is called.
     */
    protected suspend fun handleAfterLoad()

    /**
     * Called possibly multiple times after the plugin is enabled.
     * This is called after Minix has completed its internal enabling process.
     * Check your platforms documentation for more information of when this is called.
     */
    protected suspend fun handleAfterEnable()
}
