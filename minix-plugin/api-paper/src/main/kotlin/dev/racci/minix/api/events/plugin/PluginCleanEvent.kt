package dev.racci.minix.api.events.plugin

import dev.racci.minix.api.events.CompanionEventHandler
import dev.racci.minix.api.plugin.MinixPlugin
import org.bukkit.event.HandlerList

/** Called when a plugin is removed and all references to it are cleaned up. */
public actual class PluginCleanEvent internal actual constructor(plugin: MinixPlugin) : MinixPluginEvent(plugin, true) {
    public companion object : CompanionEventHandler() {
        @JvmStatic override fun getHandlerList(): HandlerList = super.getHandlerList()
    }
}
