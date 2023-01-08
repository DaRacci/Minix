package dev.racci.minix.integrations

import org.apiguardian.api.API

/**
 * A common class for integrating with other plugins.
 * Integrations should be annotated with [dev.racci.minix.integrations.annotations.IntegrationPlugin] to declare the target plugin.
 * If the integration is not self-acting, it should be annotated with [dev.racci.minix.integrations.annotations.IntegrationManager] to declare the manager.
 *
 * @see dev.racci.minix.integrations.regions.RegionIntegration
 */
@API(status = API.Status.EXPERIMENTAL, since = "4.0.0")
public interface Integration {

    /** Called when the integration is registered. */
    public suspend fun handleLoad(): Unit = Unit

    /** Called when your plugin is enabled. */
    public suspend fun handleEnable(): Unit = Unit

    /** Called when the integration is unregistered. */
    public suspend fun handleUnload(): Unit = Unit
}
