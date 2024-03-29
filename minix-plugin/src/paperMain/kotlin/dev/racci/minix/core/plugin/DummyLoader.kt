package dev.racci.minix.core.plugin

import arrow.core.filterIsInstance
import dev.racci.minix.api.extensions.collections.findKCallable
import dev.racci.minix.api.extensions.pluginManager
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.jumper.MinixInit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.SimplePluginManager
import org.bukkit.plugin.java.PluginClassLoader
import java.lang.ref.WeakReference
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

internal class DummyLoader(
    description: PluginDescriptionFile,
    initPlugin: MinixInit,
    classLoader: PluginClassLoader
) {
    init {
        // Update the main class path
        description.setValue("main", "dev.racci.minix.core.MinixImpl")

        // These values need to be null when a JavaPlugin class creates a new instance on the classloader.
        classLoader.setValue<PluginClassLoader, Plugin?>("plugin", null)
        classLoader.setValue<PluginClassLoader, Plugin?>("pluginInit", null)

        val minix = Minix(WeakReference(initPlugin))
        classLoader.setValue<PluginClassLoader, Plugin?>("plugin", minix)
        classLoader.setValue<PluginClassLoader, Plugin?>("pluginInit", minix)
        minix.onLoad()

        Thread {
            // Delay for a nanosecond to allow the method to return, otherwise the plugin won't have been added to these lists/maps yet.
            Thread.sleep(0, 1)

            val plugins = (pluginManager as SimplePluginManager).getValue<SimplePluginManager, ArrayList<Plugin>>("plugins")
            plugins.remove(initPlugin)
            plugins.add(minix)
            val lookupNames = (pluginManager as SimplePluginManager).getValue<SimplePluginManager, HashMap<String, Plugin>>("lookupNames")
            lookupNames.remove(initPlugin.name.lowercase())
            lookupNames[minix.name.lowercase()] = minix
        }.start()
    }

    internal companion object {
        inline fun <reified T : Any, R : Any> T.getValue(field: String): R = T::class.memberProperties.findKCallable(field, false)
            .tap { it.isAccessible = true }
            .map { it.get(this).castOrThrow<R>() to it }
            .tap { it.second.isAccessible = false }
            .orNull()!!.first

        inline fun <reified T : Any, reified V> T.setValue(
            field: String,
            value: V
        ) = T::class.memberProperties.findKCallable(field)
            .tap { it.isAccessible = true }
            .filterIsInstance<KMutableProperty1<*, *>>()
            .map { it.setter.call(this, value) }
    }
}
