package me.racci.raccicore.core

import me.racci.raccicore.RacciPlugin
import me.racci.raccicore.extensions.pm
import kotlin.reflect.KClass

internal object PluginManager {

    private val loadedPlugins = HashMap<KClass<out RacciPlugin>, RacciPlugin>()

    fun close() {
        loadedPlugins.values.forEach(pm::disablePlugin)
        loadedPlugins.clear()
    }

    fun add(plugin: RacciPlugin) {
        loadedPlugins[plugin::class] = plugin}
    fun remove(plugin: RacciPlugin) {
        loadedPlugins.remove(plugin::class)}

}