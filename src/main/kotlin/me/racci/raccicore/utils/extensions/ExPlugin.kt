package me.racci.raccicore.utils.extensions

import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

fun Plugin.registerEvents(
        vararg listeners: Listener
) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

fun WithPlugin<*>.registerEvents(
        vararg listeners: Listener
) = plugin.registerEvents(*listeners)

interface WithPlugin<T : Plugin> { val plugin: T }
