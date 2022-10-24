package dev.racci.minix.api.events.plugin

import dev.racci.minix.api.plugin.MinixPlugin

/**
 * Represents a PluginEvent.
 *
 * ## Includes Pre-done handlers and cancellable params However, you still need to add your own static handler.
 */
public actual abstract class MinixPluginEvent internal actual constructor(plugin: MinixPlugin, async: Boolean) {
    actual val plugin: MinixPlugin
        get() = TODO("Not yet implemented")
    actual val async: Boolean
        get() = TODO("Not yet implemented")
    actual var cancelled: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}

}