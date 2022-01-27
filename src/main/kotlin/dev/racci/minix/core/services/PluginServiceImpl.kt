package dev.racci.minix.core.services

import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.pm
import dev.racci.minix.api.flow.eventFlow
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.collections.CollectionUtils.clear
import kotlinx.coroutines.flow.filterIsInstance
import org.bukkit.event.EventPriority
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
import kotlin.reflect.KClass

class PluginServiceImpl(override val plugin: MinixPlugin) : Extension(), PluginService {

    override val name = "Plugin Service"

    override val loadedPlugins = HashMap<KClass<out MinixPlugin>, MinixPlugin>()

    override suspend fun handleEnable() {

        eventFlow<PluginEnableEvent>(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true,
        ).filterIsInstance<MinixPlugin>().collect { plugin ->
            minix.log.debug { "Handling enable for ${plugin.name}" }
            loadedPlugins[plugin::class] = plugin
        }

        eventFlow<PluginDisableEvent>(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true,
        ).filterIsInstance<MinixPlugin>().collect { plugin ->
            minix.log.debug { "Handle disable for ${plugin.name}" }
            loadedPlugins -= plugin::class
        }
    }

    override suspend fun handleUnload() {
        loadedPlugins.clear(pm::disablePlugin)
    }
}
