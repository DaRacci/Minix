package dev.racci.minix.api.events.plugin

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.flowbus.FlowBus

/**
 * An event that is posted to the global [FlowBus]
 * whenever a [MinixPlugin]'s state changes.
 *
 * @property plugin The plugin that changed state.
 * @property state The new state of the plugin.
 */
public class MinixPluginStateEvent internal constructor(
    plugin: MinixPlugin,
    public val state: State
) : MinixPluginEvent(plugin) {
    public enum class State {
        LOAD,
        ENABLE,
        RELOAD,
        DISABLE,
        UNLOAD
    }
}
