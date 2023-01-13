package dev.racci.minix.api.annotations

import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental

/**
 * Declares that this annotation is part of the Minix Reflective API,
 * once declared the annotation will be automatically picked up by the [dev.racci.minix.api.extension.Extension] system.
 */
@Experimental
@AvailableSince("5.0.0")
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.ANNOTATION_CLASS)
internal annotation class MinixReflectiveAPI
