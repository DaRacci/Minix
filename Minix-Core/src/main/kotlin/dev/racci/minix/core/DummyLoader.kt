package dev.racci.minix.core

import dev.racci.minix.api.extensions.pluginManager
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.SimplePluginManager
import org.bukkit.plugin.java.PluginClassLoader
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

class DummyLoader {

    fun loadPlugin(
        description: PluginDescriptionFile,
        initPlugin: MinixInit,
        classLoader: PluginClassLoader
    ) {
        // Update the main class path
        description.setValue("main", "dev.racci.minix.core.MinixImpl")

        // These values need to be null when a JavaPlugin class creates a new instance on the classloader
        classLoader.setValue<PluginClassLoader, Plugin?>("plugin", null)
        classLoader.setValue<PluginClassLoader, Plugin?>("pluginInit", null)
        val minix = MinixImpl()
        classLoader.setValue<PluginClassLoader, Plugin?>("plugin", minix)
        classLoader.setValue<PluginClassLoader, Plugin?>("pluginInit", minix)
        minix.onLoad()

//        val loaders = getValue<JavaPluginLoader, CopyOnWriteArrayList<PluginClassLoader>>("loaders", pluginLoader)

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

    private inline fun <reified T : Any, R> getValue(field: String, obj: T, relock: Boolean = false): R {
        val prop = T::class.memberProperties.find { it.name == field } ?: throw IllegalArgumentException("Field $field not found in ${obj::class.qualifiedName}")
        var wasLocked = false

        if (!prop.isAccessible) {
            wasLocked = true
            prop.isAccessible = true
        }

        val value = prop.get(obj) as R

        if (wasLocked && relock) {
            prop.isAccessible = false
        }

        return value
    }

    private inline fun <reified T : Any, reified V> setValue(
        field: String,
        obj: T,
        value: V,
        relock: Boolean = false
    ) {
        val prop = T::class.memberProperties.find { it.name == field } ?: throw IllegalArgumentException("Field $field not found in ${obj::class.qualifiedName}")
        var wasLocked = false

        if (!prop.isAccessible) {
            wasLocked = true
            prop.isAccessible = true
        }

        prop.javaField!!.set(obj, value)

        if (wasLocked && relock) {
            prop.isAccessible = false
        }
    }

    private inline fun <reified T : Any, reified V> T.setValue(field: String, value: V) {
        setValue(field, this, value, true)
    }
}
