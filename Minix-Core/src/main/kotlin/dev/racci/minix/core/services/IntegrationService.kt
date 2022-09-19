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

        this.integrations.registerAll()
    }

    override suspend fun handleEnable() {
        this.registerEvents()
        this.integrations.filter { it.value.loaded }.forEach { it.value.get().getOrThrow().handleEnable() }
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
        override fun predicateLoadable() = ::enabledPlugins.isInitialized && enabledPlugins.contains(descriptor)
        override suspend fun onLoad(): Integration {
            logger.info { "Loading integration with ${integrationLoader.pluginName}" }
            val integration = integrationLoader.callback.invoke(enabledPlugins[descriptor]!!)
            integration.handleLoad()
            return integration
        }
        override suspend fun onUnload(value: Integration) {
            logger.info { "Unloading integration with ${integrationLoader.pluginName}" }
            value.handleUnload()
        }
    }

    private fun registerEvents() {
        event<PluginEnableEvent> {
            val descriptor = this.plugin.name.lowercase()
            enabledPlugins = enabledPlugins.put(descriptor, this.plugin)
            integrations.register(descriptor)
            integrations[descriptor]?.get(false)?.onSuccess { it.handleEnable() }
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
