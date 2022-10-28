package dev.racci.minix.integrations

internal data class IntegrationRef<I>(
    val ref: I,
    val pluginName: String
)
