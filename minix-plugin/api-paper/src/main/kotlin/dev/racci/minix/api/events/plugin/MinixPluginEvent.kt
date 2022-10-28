package dev.racci.minix.api.events.plugin

import dev.racci.minix.api.events.MinixEvent
import dev.racci.minix.api.plugin.MinixPlugin

/**
 * Represents a PluginEvent.
 *
 * ## Includes Pre-done handlers and cancellable params However, you still need to add your own static handler.
 */
public actual abstract class MinixPluginEvent internal constructor(
    public actual open val plugin: MinixPlugin,
    async: Boolean = true
) : MinixEvent(async) {
    internal actual constructor(plugin: MinixPlugin) : this(plugin, true)
}
