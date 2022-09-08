package dev.racci.minix.core.services

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

class IntegrationService(override val plugin: Minix) : Extension<Minix>() {
    private lateinit var ENABLED_PLUGINS: PersistentMap<String, Plugin>
    private var integrations = RegisteringMap<String, Integration>()

    override suspend fun handleLoad() {
        this.ENABLED_PLUGINS = pluginManager.plugins
            .associateBy { it.name.lowercase() }
            .toPersistentHashMap()
    }

    override suspend fun handleEnable() {
        this.registerEvents()
    }

    override suspend fun handleUnload() {
        this.integrations.unregisterAll()
        this.ENABLED_PLUGINS = persistentHashMapOf()
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
        override fun predicateLoadable() = ENABLED_PLUGINS.contains(descriptor)
        override suspend fun onLoad(): Integration {
//            plugin.log.info
            return integrationLoader.callback.invoke(ENABLED_PLUGINS[descriptor]!!).also { integration -> integration.handleLoad() }
        }
        override suspend fun onUnload(value: Integration) = value.handleUnload()
    }

    private fun registerEvents() {
        event<PluginEnableEvent> {
            ENABLED_PLUGINS = ENABLED_PLUGINS.put(this.plugin.name.lowercase(), this.plugin)
            integrations.register(this.plugin.name.lowercase())
        }

        event<PluginDisableEvent> {
            ENABLED_PLUGINS = ENABLED_PLUGINS.remove(this.plugin.name.lowercase(), this.plugin)
            integrations.unregister(this.plugin.name.lowercase())
        }
    }

    internal data class IntegrationLoader(
        val pluginName: String,
        val callback: (Plugin) -> Integration
    )
}
