package dev.racci.minix.api.annotations

import dev.racci.minix.api.extension.ExtensionSkeleton
import org.apiguardian.api.API
import org.jetbrains.annotations.ApiStatus.Internal

/**
 * Informs the plugin service that this class should not be unloaded / is required always.
 * This is useful for services that are required for the plugin to function.
 * [ExtensionSkeleton.handleUnload] will still be called, however, it won't be unloaded from koin, or the plugin service.
 */
@Internal
@API(status = API.Status.INTERNAL)
public annotation class DoNotUnload
