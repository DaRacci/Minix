package me.racci.raccicore.core

import me.racci.raccicore.api.extensions.KListener
import me.racci.raccicore.api.extensions.event
import me.racci.raccicore.api.extensions.pm
import me.racci.raccicore.api.lifecycle.LifecycleListener
import me.racci.raccicore.api.plugin.RacciPlugin
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent
import kotlin.reflect.KClass

class PluginManager(
    override val plugin: RacciCore
): LifecycleListener<RacciCore>, KListener<RacciCore> {

    private val loadedPlugins = HashMap<KClass<out RacciPlugin>, RacciPlugin>()

    override suspend fun onEnable() {
        event<PluginEnableEvent> {
            if(plugin is RacciPlugin) {
                loadedPlugins[(plugin as RacciPlugin)::class] = plugin as RacciPlugin
            }
        }
        event<PluginDisableEvent> {
            if(plugin is RacciPlugin) {
                loadedPlugins.remove(plugin::class)
            }
        }
    }

    override suspend fun onDisable() {
        loadedPlugins.values.forEach(pm::disablePlugin)
        loadedPlugins.clear()
    }

}