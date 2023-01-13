package dev.racci.minix.api.events.plugin

import dev.racci.minix.api.events.MinixEvent
import dev.racci.minix.api.plugin.MinixPlugin

/**
 * Represents a PluginEvent.
 *
 * ## Includes Pre-done handlers, you still need to add your own static handler.
 */
public expect abstract class MinixPluginEvent internal constructor(
    plugin: MinixPlugin
) : MinixEvent {

    public val plugin: MinixPlugin
}
