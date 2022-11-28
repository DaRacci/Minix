package dev.racci.minix.core.services

import arrow.core.getOrElse
import com.google.common.collect.MultimapBuilder
import dev.racci.minix.api.collections.registering.MutableRegisteringMap
import dev.racci.minix.api.collections.registering.registeringMapOf
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.pluginManager
import dev.racci.minix.api.lifecycles.Loadable
import dev.racci.minix.api.plugin.PlatformPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.core.data.IntegrationLoader
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.flowbus.subscribe
import dev.racci.minix.integrations.Integration
import dev.racci.minix.integrations.IntegrationManager
import dev.racci.minix.integrations.annotations.MappedIntegration
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toPersistentHashMap
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
import org.bukkit.plugin.Plugin
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.findAnnotation

public actual class IntegrationService(override val plugin: Minix) : Extension<Minix>() {
    private lateinit var enabledPlugins: PersistentMap<String, Plugin>
    internal actual val integrations: MutableRegisteringMap<String, Integration> = registeringMapOf()

    actual override suspend fun handleLoad() {
        this.enabledPlugins = pluginManager.plugins
            .associateBy { it.name.lowercase() }
            .toPersistentHashMap()

        this.integrations.tryRegisterAll()
    }

    actual override suspend fun handleEnable() {
        this.registerEvents()
        this.integrations.entries.forEach { it.value.handleEnable() }
    }

    actual override suspend fun handleUnload() {
        this.integrations.unregisterAll()
        this.enabledPlugins = persistentHashMapOf()
    }

    internal suspend fun serverLoad() {
        this.integrations.tryRegisterAll()
    }

    internal actual fun registerIntegration(integration: IntegrationLoader) {
        val descriptor = integration.pluginName.lowercase()
        this.integrations.put(descriptor, getLoadable(descriptor, integration))
        this.integrations.register(descriptor)
    }

    internal actual fun getLoadable(
        descriptor: String,
        integrationLoader: IntegrationLoader
    ): Loadable<Integration> = object : Loadable<Integration>() {
        override fun predicateLoadable() = ::enabledPlugins.isInitialized && enabledPlugins.contains(descriptor)
        override suspend fun onLoad(): Integration {
            logger.info { "Loading integration with ${integrationLoader.pluginName}" }
            val integration = integrationLoader.callback.invoke(PlatformPlugin.wrapped(enabledPlugins[descriptor]!!))
            integration.handleLoad()

            val managerKClass = integration::class.findAnnotation<MappedIntegration>()!!.integrationManager
            val manager = (managerKClass.objectInstance ?: managerKClass.companionObjectInstance) as IntegrationManager<Integration>
            manager.register(integration)

            return integration
        }
        override suspend fun onUnload(value: Integration) {
            logger.info { "Unloading integration with ${integrationLoader.pluginName}" }
            value.handleUnload()
        }
    }

    private fun registerEvents() {
        val waitingEnable = MultimapBuilder.hashKeys().hashSetValues().build<Plugin, Integration>()
        subscribe<PluginEnableEvent> {
            val descriptor = this.plugin.name.lowercase()
            enabledPlugins = enabledPlugins.put(descriptor, this.plugin)

            if (waitingEnable.containsKey(this.plugin)) {
                waitingEnable[this.plugin].forEach { integration -> integration.handleEnable() }
                waitingEnable.removeAll(this.plugin)
            }

            integrations.register(descriptor)
            integrations[descriptor]?.let { integration ->
                val plugin = PluginService.fromClass(integration::class).getOrElse { error("No plugin found for integration ${integration::class}") }
                when {
                    !plugin.enabled -> waitingEnable.put(plugin, integration)
                    else -> integration.handleEnable()
                }
            }
        }

        subscribe<PluginDisableEvent> {
            enabledPlugins = enabledPlugins.remove(this.plugin.name.lowercase(), this.plugin)
            integrations.unregister(this.plugin.name.lowercase())
        }
    }
}
