package dev.racci.minix.api.annotations

import dev.racci.minix.api.plugin.MinixPlugin
import kotlin.reflect.KClass

/**
 * Marks a plugins information via an annotation instead of overriding the [MinixPlugin] interface fields.
 *
 * @property bStatsId The BStats ID of the plugin
 * @property bindToKClass The class that this plugin binds to, if not specified, the plugin will bind to itself
 * @property extensions The extensions that this plugin provides
 */
@Target(AnnotationTarget.CLASS)
annotation class MappedPlugin(
    val bStatsId: Int = -1,
    val bindToKClass: KClass<*> = MinixPlugin::class,
    val extensions: Array<KClass<*>> = []
)
