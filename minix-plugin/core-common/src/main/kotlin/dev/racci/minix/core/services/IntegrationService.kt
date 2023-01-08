package dev.racci.minix.core.services

import dev.racci.minix.api.annotations.Required
import dev.racci.minix.api.collections.registering.MutableRegisteringMap
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.lifecycles.Loadable
import dev.racci.minix.core.data.IntegrationLoader
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.integrations.Integration

@Required
public expect class IntegrationService : Extension<Minix> {
    internal val integrations: MutableRegisteringMap<String, Integration>

    override suspend fun handleLoad()

    override suspend fun handleEnable()

    override suspend fun handleUnload()

    internal fun registerIntegration(integration: IntegrationLoader)

    internal fun getLoadable(
        descriptor: String,
        integrationLoader: IntegrationLoader
    ): Loadable<Integration>
}
