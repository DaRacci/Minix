package dev.racci.minix.integrations.annotations

import dev.racci.minix.integrations.IntegrationManager
import org.apiguardian.api.API
import kotlin.reflect.KClass

/**
 * Marks an Integrations class to be registered.
 *
 * @property pluginName The name of the plugin that this integration is for.
 * @property integrationManager The IntegrationManager that handles this integration type, if set to [IntegrationManager], marks this integration as anonymous and self-acting.
 */
@API(status = API.Status.EXPERIMENTAL, since = "4.0.0")
public annotation class MappedIntegration(
    val pluginName: String,
    val integrationManager: KClass<out IntegrationManager<*>> = IntegrationManager::class
)
