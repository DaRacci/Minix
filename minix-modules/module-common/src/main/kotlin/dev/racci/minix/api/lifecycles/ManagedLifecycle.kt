package dev.racci.minix.api.lifecycles

import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
@AvailableSince("4.3.0")
public interface ManagedLifecycle : Lifecycle {

    /** Called after [Lifecycle.handleLoad] and the objects manager has finished its create|load process. */
    @Suppress("kotlin:S6318")
    public suspend fun handlePostLoad() { /* no-op */ }

    /** Called after [Lifecycle.handleDestroy] and the objects manager has finished its destroy|dispose|unload process. */
    @Suppress("kotlin:S6318")
    public suspend fun handlePostUnload() { /* no-op */ }
}
