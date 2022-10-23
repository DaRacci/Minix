package dev.racci.minix.integrations

import dev.racci.minix.integrations.annotations.IntegrationTarget
import dev.racci.minix.integrations.annotations.MappedIntegration
import org.apiguardian.api.API
import kotlin.reflect.full.findAnnotation

/**
 * A common class for integrating with other plugins.
 * Integrations should be annotated with [MappedIntegration] to be registered.
 *
 * @see RegionIntegration
 */
@API(status = API.Status.EXPERIMENTAL)
public interface Integration {

    /** The integrations plugin name. */
    public val pluginName: String get() = this::class.findAnnotation<IntegrationTarget>()?.targetPlugin ?: "Unknown"

    /** Called when the integration is registered. */
    public suspend fun handleLoad(): Unit = Unit

    /** Called when your plugin is enabled. */
    public suspend fun handleEnable(): Unit = Unit

    /** Called when the integration is unregistered. */
    public suspend fun handleUnload(): Unit = Unit
}
