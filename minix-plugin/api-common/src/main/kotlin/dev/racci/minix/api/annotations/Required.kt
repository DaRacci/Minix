package dev.racci.minix.api.annotations

import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental

/**
 * Declares that the extension must always be loaded and if it fails to load, the plugin will be disabled.
 */
@Experimental
@AvailableSince("5.0.0")
@MinixReflectiveAPI
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Required
