package dev.racci.minix.api.plugin

import dev.racci.minix.api.data.PluginUpdater
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.updater.Version
import org.apiguardian.api.API
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent

@API(status = API.Status.MAINTAINED, since = "1.0.0")
interface SusPlugin : Plugin, KoinComponent {

    /** The plugin's [MinixLogger] instance. */
    val log: MinixLogger

    /** The plugin's bStats ID. */
    val bStatsId: Int?

    /** The plugin's bStats metrics instance. */
    val metrics: Metrics?

    /** The plugin's version instance. */
    val version: Version

    /** The plugin's auto-updater. */
    val updater: PluginUpdater?

    /**
     * Handles the enabling of the plugin.
     * This is called while the plugin is considered enabled.
     * This is called before Minix starts its internal enabling process.
     *
     * ## Enabling can occur multiple times during the lifetime of the plugin.
     */
    suspend fun handleEnable() = Unit

    /**
     * Handles the loading of the plugin.
     * This is called while the plugin considered disabled.
     * This is called before Minix starts its internal loading process.
     *
     * ## Loading only occurs once when the server loads this plugin.
     */
    suspend fun handleLoad() = Unit

    /**
     * Handles the unloading of the plugin.
     * This is called while the plugin is considered disabled.
     *
     * ## Unloading only occurs once when server is shutting down.
     */
    suspend fun handleUnload() = Unit

    /**
     * Handles the disabling of the plugin.
     * This is called when the plugin is considered disabled.
     *
     * ## Unloading can occur multiple times during the lifetime of the extension.
     */
    suspend fun handleDisable() = Unit

    /**
     * Handles the after-loading of the plugin.
     * This is called while the plugin is considered disabled.
     * This is called after Minix has completed its internal loading process.
     *
     * ## After-load only occurs once when the server loads this plugin.
     */
    suspend fun handleAfterLoad() = Unit

    /**
     * Handles the after-loading of the plugin.
     * This is called while the plugin is disabling and is considered disabled.
     * This is called after Minix has completed its internal enabling process.
     *
     * ## Unloading only occurs once when server is shutting down.
     */
    suspend fun handleAfterEnable() = Unit
}
