package dev.racci.minix.api.plugin

import dev.racci.minix.api.data.PluginUpdater
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.updater.Version
import org.apiguardian.api.API
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.Plugin
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval
import org.koin.core.component.KoinComponent
import kotlin.reflect.KClass

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

    @Deprecated("Use the MappedPlugin annotation instead.")
    @get:ScheduledForRemoval(inVersion = "4.0.0")
    val bindToKClass: KClass<out MinixPlugin>?

    /**
     * This is called when the server picks up your Plugin and has begun loading it.
     */
    suspend fun handleLoad() = Unit

    /**
     * This is called Once the Plugin is ready to accept and register
     * events, commands etc.
     */
    suspend fun handleEnable() = Unit

    /**
     * This will be called after your Plugin is done loading
     * and Minix has finished its loading process for your Plugin.
     */
    suspend fun handleAfterLoad() = Unit

    /** This is called after [handleEnable] and after plugin service has finished processing the plugin. */
    suspend fun handleAfterEnable() = Unit

    /**
     * This is triggered when your Plugin is being disabled by the Server,
     * Please use this to clean up your Plugin to not leak resources.
     */
    suspend fun handleDisable() = Unit
}
