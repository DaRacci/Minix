package dev.racci.minix.api.lifecycles

public interface ComplexManagedLifecycle : ComplexLifecycle, ManagedLifecycle {

    /** Called after [ComplexLifecycle.handleEnable] and the objects manager has finished its enable|start process. */
    public suspend fun handlePostEnable() { /* no-op */ }

    /** Called after [ComplexLifecycle.handleDisable] and the objects manager has finished its disable|stop process. */
    public suspend fun handlePostDisable() { /* no-op */ }
}
