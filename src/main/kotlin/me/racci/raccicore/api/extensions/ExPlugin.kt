@file:Suppress("UNUSED", "UNCHECKED_CAST")
package me.racci.raccicore.api.extensions

import me.racci.raccicore.api.plugin.RacciPlugin
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

fun Plugin.registerEvents(
        vararg listeners: Listener
) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

fun WithPlugin<*>.registerEvents(
        vararg listeners: Listener
) = plugin.registerEvents(*listeners)

interface WithPlugin<T : RacciPlugin> { val plugin: T }