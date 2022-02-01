@file:Suppress("UNUSED", "UNCHECKED_CAST")

package dev.racci.minix.api.extensions

import dev.racci.minix.api.plugin.MinixPlugin
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

fun Plugin.registerEvents(
    vararg listeners: Listener,
) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

fun WithPlugin<*>.registerEvents(
    vararg listeners: Listener
) = plugin.registerEvents(*listeners)

interface WithPlugin<T : MinixPlugin> {

    val plugin: T
}
