package dev.racci.minix.api.lifecycles

import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
@AvailableSince("4.3.0")
public interface Lifecycle {

    /** Called when this object is created|loaded */
    @Suppress("kotlin:S6318")
    public suspend fun handleLoad() { /* no-op */ }

    /** Called when this object is destroyed|disposed|unloaded */
    @Suppress("kotlin:S6318")
    public suspend fun handleUnload() { /* no-op */ }
}
