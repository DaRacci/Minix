package dev.racci.minix.integrations.annotations

import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
@AvailableSince("5.0.0")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class IntegrationPlugin(val pluginName: String)
