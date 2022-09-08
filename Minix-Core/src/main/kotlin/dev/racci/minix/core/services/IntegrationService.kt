package dev.racci.minix.core.services

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.collections.RegisteringMap
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.pluginManager
import dev.racci.minix.api.integrations.Integration
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.utils.Loadable
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toPersistentHashMap
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.plugin.Plugin

@MappedExtension(Minix::class, "Integration Service")
class IntegrationService(override val plugin: Minix) : Extension<Minix>() {
    private lateinit var enabledPlugins: PersistentMap<String, Plugin>
    private val integrations = RegisteringMap<String, Integration>()

    override suspend fun handleLoad() {
        this.enabledPlugins = pluginManager.plugins
            .associateBy { it.name.lowercase() }
            .toPersistentHashMap()
    }

    override suspend fun handleEnable() {
        this.registerEvents()
    }

    override suspend fun handleUnload() {
        this.integrations.unregisterAll()
        this.enabledPlugins = persistentHashMapOf()
    }

    internal fun registerIntegration(integration: IntegrationLoader) {
        val descriptor = integration.pluginName.lowercase()
        this.integrations.put(descriptor, getLoadable(descriptor, integration))
        this.integrations.register(descriptor)
    }

    private fun getLoadable(
        descriptor: String,
        integrationLoader: IntegrationLoader
    ): Loadable<Integration> = object : Loadable<Integration>() {
        override fun predicateLoadable() = enabledPlugins.contains(descriptor)
        override suspend fun onLoad(): Integration {
            plugin.log.info { "Loading integration with ${integrationLoader.pluginName}" }
            return integrationLoader.callback.invoke(enabledPlugins[descriptor]!!).also { integration -> integration.handleLoad() }
        }
        override suspend fun onUnload(value: Integration) {
            plugin.log.info { "Unloading integration with ${integrationLoader.pluginName}" }
            value.handleUnload()
        }
    }

    private fun registerEvents() {
        event<PluginEnableEvent> {
            enabledPlugins = enabledPlugins.put(this.plugin.name.lowercase(), this.plugin)
            integrations.register(this.plugin.name.lowercase())
        }

        event<PluginDisableEvent> {
            enabledPlugins = enabledPlugins.remove(this.plugin.name.lowercase(), this.plugin)
            integrations.unregister(this.plugin.name.lowercase())
        }
    }

    internal data class IntegrationLoader(
        val pluginName: String,
        val callback: (Plugin) -> Integration
    )
}
