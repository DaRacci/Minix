package dev.racci.minix.api.annotations

import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import kotlin.reflect.KClass

/**
 * Provides the [PluginService] with information about your plugin.
 *
 * @property bStatsId The BStats ID of the plugin
 * @property bindToKClass The class that this plugin binds to, if not specified, the plugin will bind to itself.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MappedPlugin(
    val bStatsId: Int = -1,
    val bindToKClass: KClass<*> = MinixPlugin::class
)
