package dev.racci.minix.core.integrations.placeholderapi

import dev.racci.minix.api.annotations.MappedIntegration
import dev.racci.minix.api.integrations.placeholders.PlaceholderIntegration
import dev.racci.minix.api.integrations.placeholders.PlaceholderManager
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin

@MappedIntegration("PlaceholderAPI", Minix::class, PlaceholderManager::class)
class MinixPlaceholderIntegration(override val plugin: MinixPlugin) : PlaceholderIntegration() {

    init {
        registerPlaceholder("version") { plugin.description.version }
    }
}
