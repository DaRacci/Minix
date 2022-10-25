package dev.racci.minix.api.lifecycles

public interface ComplexLifecycle : Lifecycle {

    /** Called when this object is enabled|started */
    public suspend fun handleEnable() { /* no-op */ }

    /** Called when this object is should reload its configuration/data */
    public suspend fun handleReload() { /* no-op */ }

    /** Called when this object is disabled|stopped */
    public suspend fun handleDisable() { /* no-op */ }
}
