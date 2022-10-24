package dev.racci.minix.api.events.plugin

import dev.racci.minix.api.events.MinixEvent
import dev.racci.minix.api.plugin.MinixPlugin
import org.bukkit.event.HandlerList
import org.bukkit.event.server.ServerEvent

/**
 * Represents a PluginEvent.
 *
 * ## Includes Pre-done handlers and cancellable params However, you still need to add your own static handler.
 */
public actual abstract class MinixPluginEvent internal constructor(
    public actual val plugin: MinixPlugin,
    async: Boolean
) : ServerEvent(async) {
    final override fun getHandlers(): HandlerList = MinixEvent.handlerMap[this::class]
}
