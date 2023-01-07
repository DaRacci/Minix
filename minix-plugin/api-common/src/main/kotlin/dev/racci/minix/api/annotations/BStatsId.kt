package dev.racci.minix.api.annotations

import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
@AvailableSince("5.0.0")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class BStatsId(val id: Int)
