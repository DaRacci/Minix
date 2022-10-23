package dev.racci.minix.integrations.placeholderapi

import dev.racci.minix.api.integrations.placeholders.PlaceholderIntegration
import dev.racci.minix.api.integrations.placeholders.PlaceholderManager
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.integrations.annotations.IntegrationTarget
import dev.racci.minix.integrations.annotations.MappedIntegration
import org.koin.core.annotation.Singleton

@Singleton
@MappedIntegration("PlaceholderAPI", PlaceholderManager::class)
@IntegrationTarget("PlaceholderAPI")
class MinixPlaceholderIntegration(override val plugin: MinixPlugin) : PlaceholderIntegration() {

    init {
        registerPlaceholder("version") { plugin.description.version }
    }
}
