@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
package dev.racci.minix.core

import dev.racci.minix.api.extensions.pluginManager
import io.github.slimjar.app.builder.ApplicationBuilder
import org.bukkit.plugin.InvalidPluginException
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.SimplePluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import org.bukkit.plugin.java.PluginClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

/**
 * A really fucking stupid and janky way of fixing the conflict with eco.
 */
class MinixInit : JavaPlugin() {

    override fun onLoad() {
        val builder = ApplicationBuilder.appending("Minix")

        val folder = Path.of("$dataFolder/libraries")
        if (!Files.exists(folder)) folder.toFile().mkdirs()
        builder.downloadDirectoryPath(folder)
        builder.build()

        loadPlugin()
    }

    override fun onEnable() {
        println("Incorrect enabling.")
    }

    override fun onDisable() {
        println("Incorrect disabling.")
    }

    @Throws(InvalidPluginException::class)
    private fun loadPlugin() {
        description.setValue("main", "dev.racci.minix.core.MinixImpl")
        description.setValue("name", "Minix")
        description.setValue("prefix", "Minix")

        val loaders = getValue<JavaPluginLoader, CopyOnWriteArrayList<PluginClassLoader>>("loaders", pluginLoader as JavaPluginLoader)

        var minix: MinixImpl? = null
        for (loader in loaders) {
            if (loader.plugin != this) continue
            loader.setValue<PluginClassLoader, Plugin?>("plugin", null)
            loader.setValue<PluginClassLoader, Plugin?>("pluginInit", null)
            minix = MinixImpl()
            loader.setValue("plugin", minix)
            minix.onLoad()
            break
        }

        if (minix == null) {
            throw InvalidPluginException("Minix could not be loaded.")
        }

        val plugins = getValue<SimplePluginManager, ArrayList<Plugin>>("plugins", pluginManager as SimplePluginManager)
        plugins.remove(this)
        plugins.add(minix)

        val lookupNames = getValue<SimplePluginManager, HashMap<String, Plugin>>("lookupNames", pluginManager as SimplePluginManager)
        lookupNames.remove(this.name.lowercase())
        lookupNames[minix.name.lowercase()] = minix
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

    private inline fun <reified T : Any, R> T.getValue(field: String): R {
        return getValue(field, this, true)
    }

    private inline fun <reified T : Any, reified V> T.setValue(field: String, value: V) {
        setValue(field, this, value, true)
    }
}
