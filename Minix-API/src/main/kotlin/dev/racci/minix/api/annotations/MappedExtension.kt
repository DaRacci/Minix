package dev.racci.minix.api.annotations

import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.plugin.MinixPlugin
import kotlin.reflect.KClass

/**
 * Marks an extension class
 * The extension class must extend [Extension] and have a constructor with a single parameter of type [MinixPlugin]
 *
 * @property parent The parent KClass of the plugin
 * @property name The unique name of the extension
 * @property dependencies The other extensions that this requires to be loaded, Note this must be an [Extension] however it needs to be compiled as any KClass
 * @property bindToKClass The class that this extension binds to, if not specified, the extension will bind to itself
 */
@Target(AnnotationTarget.CLASS)
annotation class MappedExtension(
    val parent: KClass<*>,
    val name: String,
    val dependencies: Array<KClass<*>> = [],
    val bindToKClass: KClass<*> = Extension::class
)
