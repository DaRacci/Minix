package dev.racci.minix.api.annotations

import dev.racci.minix.api.services.DataService
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import kotlin.reflect.KClass

/**
 * Marks a Configuration class for [DataService].
 * Classes annotated with this annotation must also have use [ConfigSerializable].
 *
 * @property parent The KClass of your plugin.
 * @property file The name of the file excluding the extension.
 * @property serializers An array of the serializers, these should be in groups of two with the first being the class, and the second being the serializer.
 */
@Target(AnnotationTarget.CLASS)
annotation class MappedConfig(
    val parent: KClass<*>,
    val file: String,
    val serializers: Array<KClass<*>> = []
)
