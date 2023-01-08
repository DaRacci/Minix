package dev.racci.minix.api.annotations

import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental
import kotlin.reflect.KClass

/**
 * Defines the dependencies for this extension,
 * Each dependency will be loaded before this extension is able to load.
 * If one of the dependencies fails to load, this extension will not load either.
 *
 * @property dependencies The other extensions that this requires to be loaded, Note this must be an [dev.racci.minix.api.extension.Extension] however it needs to be compiled as any KClass.
 */
// TODO: Add support for depends between plugins, and allow specifying by QualifierValue.
@Experimental
@AvailableSince("5.0.0")
@MinixReflectiveAPI
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Depends(val dependencies: Array<KClass<*>>)
