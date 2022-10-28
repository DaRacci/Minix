package dev.racci.minix.integrations.placeholderapi

import dev.racci.minix.api.integrations.placeholders.PlaceholderIntegration
import dev.racci.minix.api.integrations.placeholders.PlaceholderManager
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.integrations.annotations.MappedIntegration

@MappedIntegration("PlaceholderAPI", PlaceholderManager::class)
public class MinixPlaceholderIntegration(plugin: MinixPlugin) : PlaceholderIntegration() {

    init {
        registerPlaceholder("version") { plugin.description.version }
    }
}
