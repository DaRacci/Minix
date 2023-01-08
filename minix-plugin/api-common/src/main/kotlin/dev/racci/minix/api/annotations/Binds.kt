package dev.racci.minix.api.annotations

import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental
import kotlin.reflect.KClass

@Experimental
@AvailableSince("5.0.0")
@MinixReflectiveAPI
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Binds(val classes: Array<KClass<*>>)
