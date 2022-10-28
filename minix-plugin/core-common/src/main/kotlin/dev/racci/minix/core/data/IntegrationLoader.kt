package dev.racci.minix.core.data

import dev.racci.minix.api.plugin.PlatformPlugin
import dev.racci.minix.integrations.Integration

internal data class IntegrationLoader(
    val pluginName: String,
    val callback: (PlatformPlugin) -> Integration
)
