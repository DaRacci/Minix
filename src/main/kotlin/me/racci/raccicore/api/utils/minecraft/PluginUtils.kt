@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")
package me.racci.raccicore.api.utils.minecraft

import me.racci.raccicore.api.extensions.pm
import me.racci.raccicore.api.plugin.RacciPlugin
import org.bukkit.plugin.Plugin
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

object PluginUtils {

    /**
     * Delegate that returns the plugin dependency if the plugin is installed in the server
     * otherwise, returns null
     */
    inline fun <reified T: Plugin> RacciPlugin.softDepend(
        pluginName: String,
    ) = softDepend(T::class, pluginName)

    fun <T: Plugin> RacciPlugin.softDepend(
        type: KClass<T>,
        pluginName: String
    ) = SoftDependencyDelegate(pluginName, type)

    /**
     * Delegate that returns the plugin dependency, disable the plugin if the plugin
     * is not available.
     */
    inline fun <reified T: Plugin> RacciPlugin.depend(
        pluginName: String
    ) = depend(T::class, pluginName)

    fun <T: Plugin> RacciPlugin.depend(
        type: KClass<T>,
        pluginName: String
    ) = DependencyDelegate(pluginName, type)

    class DependencyDelegate<T: Plugin>(
        val pluginName: String,
        val type: KClass<T>
    ): ReadOnlyProperty<RacciPlugin, T> {

        private var isDisabled = false
        private var cache: T? = null


        override fun getValue(
            thisRef: RacciPlugin,
            property: KProperty<*>
        ): T {
            if(cache == null) {
                val plugin = pm.getPlugin(pluginName)
                if(plugin != null) {
                    if(type.isInstance(plugin)) {
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

    class SoftDependencyDelegate<T: Plugin>(
        val pluginName: String,
        val type: KClass<T>
    ): ReadOnlyProperty<RacciPlugin, T?> {

        private var alreadySearch: Boolean = false
        private var cache: T? = null

        override fun getValue(
            thisRef: RacciPlugin,
            property: KProperty<*>
        ): T? {
            if(!alreadySearch) {
                val plugin = pm.getPlugin(pluginName) ?: return null
                alreadySearch = true
                if(type.isInstance(plugin)) {
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