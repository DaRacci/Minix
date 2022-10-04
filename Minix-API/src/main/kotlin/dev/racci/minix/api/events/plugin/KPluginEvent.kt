package dev.racci.minix.api.events.plugin

import dev.racci.minix.api.events.KEvent.Companion.handlerMap
import dev.racci.minix.api.plugin.MinixPlugin
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.server.PluginEvent

/**
 * Represents a PluginEvent.
 *
 * ## Includes Pre-done handlers and cancellable params However, you still need to add your own static handler.
 */
abstract class KPluginEvent(
    plugin: MinixPlugin
) : PluginEvent(plugin), Cancellable {

    private var cancelled: Boolean = false

    override fun isCancelled() = cancelled

    override fun setCancelled(cancel: Boolean) { cancelled = cancel }

    override fun getHandlers(): HandlerList = handlerMap[this::class]
}
