package dev.racci.minix.api.events.plugin

import dev.racci.minix.api.plugin.MinixPlugin

/** Called when a plugin is removed and all references to it are cleaned up. */
public expect class PluginCleanEvent internal constructor(plugin: MinixPlugin) : MinixPluginEvent
