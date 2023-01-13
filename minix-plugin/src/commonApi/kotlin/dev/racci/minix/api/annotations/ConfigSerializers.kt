package dev.racci.minix.api.annotations

import kotlin.reflect.KClass

/**
 * Declares additional serializers for a [dev.racci.minix.api.data.MinixConfig] class.
 *
 * @property serializers An array of the serializers, these should be in groups of two with the first being the class, and the second being the serializer.
 */
public annotation class ConfigSerializers(val serializers: Array<KClass<*>>)
