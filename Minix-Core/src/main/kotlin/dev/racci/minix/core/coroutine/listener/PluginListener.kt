package dev.racci.minix.core.coroutine.listener

import dev.racci.minix.api.coroutine.contract.Coroutine
import dev.racci.minix.api.plugin.MinixPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent

internal class PluginListener(
    private val mcCoroutine: Coroutine,
    private val plugin: MinixPlugin,
) : Listener {

    /**
     * Gets called when the plugin is disabled.
     */
    @EventHandler
    fun onPluginDisable(pluginEvent: PluginDisableEvent) {
        if (pluginEvent.plugin != plugin) {
            return
        }

        mcCoroutine.disable(pluginEvent.plugin as? MinixPlugin ?: return)
    }
}
