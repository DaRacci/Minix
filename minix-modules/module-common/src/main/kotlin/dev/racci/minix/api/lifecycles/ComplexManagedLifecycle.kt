package dev.racci.minix.api.lifecycles

import org.jetbrains.annotations.ApiStatus.AvailableSince
import org.jetbrains.annotations.ApiStatus.Experimental

@Experimental
@AvailableSince("4.3.0")
public interface ComplexManagedLifecycle : ComplexLifecycle, ManagedLifecycle {

    /** Called after [ComplexLifecycle.handleEnable] and the objects manager has finished its enable|start process. */
    @Suppress("kotlin:S6318")
    public suspend fun handlePostEnable() { /* no-op */ }

    /** Called after [ComplexLifecycle.handleDisable] and the objects manager has finished its disable|stop process. */
    @Suppress("kotlin:S6318")
    public suspend fun handlePostDisable() { /* no-op */ }
}
