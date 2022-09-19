package dev.racci.minix.core

import dev.racci.minix.api.extensions.pluginManager
import dev.racci.minix.api.extensions.reflection.accessGet
import dev.racci.minix.api.extensions.reflection.accessSet
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.utils.collections.CollectionUtils.first
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.SimplePluginManager
import org.bukkit.plugin.java.PluginClassLoader
import kotlin.reflect.full.memberProperties

class DummyLoader {

    fun loadPlugin(
        description: PluginDescriptionFile,
        initPlugin: MinixInit,
        classLoader: PluginClassLoader
    ) {
        // Update the main class path
        description.setValue("main", "dev.racci.minix.core.MinixImpl")

        // These values need to be null when a JavaPlugin class creates a new instance on the classloader.
        classLoader.setValue<PluginClassLoader, Plugin?>("plugin", null)
        classLoader.setValue<PluginClassLoader, Plugin?>("pluginInit", null)
        val minix = MinixImpl()
        classLoader.setValue<PluginClassLoader, Plugin?>("plugin", minix)
        classLoader.setValue<PluginClassLoader, Plugin?>("pluginInit", minix)
        minix.onLoad()

        Thread {
            // Delay for a nanosecond to allow the method to return, otherwise the plugin won't have been added to these lists/maps yet.
            Thread.sleep(0, 1)

            val plugins = getValue<SimplePluginManager, ArrayList<Plugin>>("plugins", pluginManager as SimplePluginManager)
            plugins.remove(initPlugin)
            plugins.add(minix)
            val lookupNames = getValue<SimplePluginManager, HashMap<String, Plugin>>("lookupNames", pluginManager as SimplePluginManager)
            lookupNames.remove(initPlugin.name.lowercase())
            lookupNames[minix.name.lowercase()] = minix
        }.start()
    }

    private inline fun <reified T : Any, R> getValue(
        field: String,
        obj: T
    ): R = T::class.memberProperties.first(field).accessGet(obj).castOrThrow()

    private inline fun <reified T : Any, reified V> T.setValue(
        field: String,
        value: V
    ) = T::class.memberProperties.first(field).accessSet(this, value)
}
