package dev.racci.minix.api.annotations

import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental

/**
 * Allocate a new [threads] sized thread-pool for this extension,
 * This will be used for all [dev.racci.minix.api.extension.Extension.launch] and coroutine related functions.
 *
 * If this annotation is not provided or is 0 the extension will use the owning plugin's thread-pool.
 *
 * @param threads The number of threads in this extensions [CoroutineDispatcher]
 * @see dev.racci.minix.api.scheduler.CoroutineScheduler Is an example that allocates 4 threads to handle task scheduling.
 * @throws IllegalArgumentException If [threads] is negative.
 */
@Experimental
@AvailableSince("5.0.0")
@MinixReflectiveAPI
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Threads(val threads: Short)
