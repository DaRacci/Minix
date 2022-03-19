package dev.racci.minix.api.annotations

import dev.racci.minix.api.services.DataService
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import kotlin.reflect.KClass

/**
 * Classes annotated with this annotation as well as [ConfigSerializable].
 * This will allow the plugin to automagically load the config using the [DataService]
 *
 * @property parent The parent class that your plugin is bound to.
 * @property file The name of the file excluding the extension.
 */
@Target(AnnotationTarget.CLASS)
annotation class MappedConfig(

    val parent: KClass<*>,
    val file: String
)
