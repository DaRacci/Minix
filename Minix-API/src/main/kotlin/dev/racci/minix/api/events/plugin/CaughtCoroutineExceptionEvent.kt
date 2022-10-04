package dev.racci.minix.api.events.plugin

import dev.racci.minix.api.events.CompanionEventHandler
import dev.racci.minix.api.plugin.MinixPlugin
import org.bukkit.event.Cancellable

class CaughtCoroutineExceptionEvent(
    plugin: MinixPlugin
) : KPluginEvent(plugin), Cancellable {
    companion object : CompanionEventHandler() {
        @JvmStatic
        override fun getHandlerList() = super.getHandlerList()
    }
}
