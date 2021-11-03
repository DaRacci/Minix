package me.racci.raccicore

import me.racci.raccicore.utils.pm
import kotlin.reflect.KClass

internal object RacciPluginHandler {
    private val loadedPlugins = HashMap<KClass<out RacciPlugin>, RacciPlugin>()

    fun close() {
        loadedPlugins.values.forEach(pm::disablePlugin)
        loadedPlugins.clear()
    }

    fun add(plugin: RacciPlugin) {loadedPlugins[plugin::class] = plugin}
    fun remove(plugin: RacciPlugin) {loadedPlugins.remove(plugin::class)}
}