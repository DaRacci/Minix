package dev.racci.minix.integrations.annotations

import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental
import kotlin.reflect.KClass

@Experimental
@AvailableSince("5.0.0")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class IntegrationManager(val kClass: KClass<*>)
