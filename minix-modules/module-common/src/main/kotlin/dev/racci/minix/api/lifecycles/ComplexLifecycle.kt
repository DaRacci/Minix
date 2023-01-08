package dev.racci.minix.api.lifecycles

import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
@AvailableSince("4.3.0")
public interface ComplexLifecycle : Lifecycle {

    /** Called when this object is enabled|started */
    @Suppress("kotlin:S6318")
    public suspend fun handleEnable() { /* no-op */ }

    /** Called when this object is should reload its configuration/data */
    @Suppress("kotlin:S6318")
    public suspend fun handleReload() { /* no-op */ }

    /** Called when this object is disabled|stopped */
    @Suppress("kotlin:S6318")
    public suspend fun handleDisable() { /* no-op */ }
}
