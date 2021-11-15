package me.racci.raccicore.extensions

import me.racci.raccicore.RacciPlugin
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

fun Plugin.registerEvents(
        vararg listeners: Listener
) = listeners.forEach { server.pluginManager.registerEvents(it, this) }

fun WithPlugin<*>.registerEvents(
        vararg listeners: Listener
) = plugin.registerEvents(*listeners)

interface WithPlugin<T : RacciPlugin> { val plugin: T }