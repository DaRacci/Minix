@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package dev.racci.minix.api.utils.minecraft

import dev.racci.minix.api.extensions.pm
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.UtilObject
import org.bukkit.plugin.Plugin
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

object PluginUtils : UtilObject {

    /**
     * Delegate that returns the plugin dependency if the plugin is installed in the server
     * otherwise, returns null.
     */
    inline fun <reified T : Plugin> MinixPlugin.softDepend(
        pluginName: String
    ) = softDepend(T::class, pluginName)

    fun <T : Plugin> MinixPlugin.softDepend(
        type: KClass<T>,
        pluginName: String
    ) = SoftDependencyDelegate(pluginName, type)

    /**
     * Delegate that returns the plugin dependency, disable the plugin if the plugin
     * is not available.
     */
    inline fun <reified T : Plugin> MinixPlugin.depend(
        pluginName: String
    ) = depend(T::class, pluginName)

    fun <T : Plugin> MinixPlugin.depend(
        type: KClass<T>,
        pluginName: String
    ) = DependencyDelegate(pluginName, type)

    class DependencyDelegate<T : Plugin>(
        val pluginName: String,
        val type: KClass<T>
    ) : ReadOnlyProperty<MinixPlugin, T> {

        // private var isDisabled = false
        private var cache: T? = null

        @Suppress("UNCHECKED_CAST")
        override fun getValue(
            thisRef: MinixPlugin,
            property: KProperty<*>
        ): T {
            if (cache == null) {
                val plugin = pm.getPlugin(pluginName)
                if (plugin != null) {
                    if (type.isInstance(plugin)) {
                        cache = plugin as T
                    } else {
                        pm.disablePlugin(thisRef)
                        error(
                            "Invalid plugin dependency with the name $pluginName: " +
                                "The plugin do not match main class with ${type.qualifiedName}."
                        )
                    }
                } else {
                    pm.disablePlugin(thisRef)
                    error("Missing plugin dependency: $pluginName")
                }
            }
            return cache!!
        }
    }

    class SoftDependencyDelegate<T : Plugin>(
        val pluginName: String,
        val type: KClass<T>
    ) : ReadOnlyProperty<MinixPlugin, T?> {

        private var alreadySearch: Boolean = false
        private var cache: T? = null

        @Suppress("UNCHECKED_CAST")
        override fun getValue(
            thisRef: MinixPlugin,
            property: KProperty<*>
        ): T? {
            if (!alreadySearch) {
                val plugin = pm.getPlugin(pluginName) ?: return null
                alreadySearch = true
                if (type.isInstance(plugin)) {
                    cache = plugin as T
                } else {
                    pm.disablePlugin(thisRef)
                    error(
                        "Invalid plugin dependency with the name $pluginName: " +
                            "The plugin do not match main class with ${type.qualifiedName}."
                    )
                }
            }
            return cache
        }
    }
}
