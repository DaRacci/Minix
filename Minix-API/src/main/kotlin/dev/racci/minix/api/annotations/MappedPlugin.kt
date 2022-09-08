package dev.racci.minix.api.annotations

import dev.racci.minix.api.plugin.MinixPlugin
import kotlin.reflect.KClass

/**
 * Marks a plugins information via an annotation instead of overriding the [MinixPlugin] interface fields.
 *
 * @property bStatsId The BStats ID of the plugin
 * @property bindToKClass The class that this plugin binds to, if not specified, the plugin will bind to itself
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MappedPlugin(
    val bStatsId: Int = -1,
    val bindToKClass: KClass<*> = MinixPlugin::class
)
