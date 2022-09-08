package dev.racci.minix.api.integrations

import dev.racci.minix.api.annotations.MappedIntegration
import dev.racci.minix.api.integrations.regions.RegionIntegration
import dev.racci.minix.api.plugin.MinixPlugin
import org.apiguardian.api.API
import kotlin.reflect.full.findAnnotation

/**
 * A common class for integrating with other plugins.
 * Integrations should be annotated with [MappedIntegration] to be registered.
 *
 * @see RegionIntegration
 */
@API(status = API.Status.EXPERIMENTAL)
interface Integration {

    /** The plugin that registered this integration. */
    val pluginRegister: MinixPlugin

    /** The integrations plugin name. */
    val pluginName: String get() = this::class.findAnnotation<MappedIntegration>()?.pluginName ?: "Unknown"

    /** Called when the integration is registered. */
    suspend fun handleLoad() = Unit

    /** Called when the integration is unregistered. */
    suspend fun handleUnload() = Unit
}
