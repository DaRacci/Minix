package dev.racci.minix.api.lifecycles

public interface ManagedLifecycle : Lifecycle {

    /** Called after [Lifecycle.handleLoad] and the objects manager has finished its create|load process. */
    public suspend fun handlePostLoad() { /* no-op */ }

    /** Called after [Lifecycle.handleDestroy] and the objects manager has finished its destroy|dispose|unload process. */
    public suspend fun handlePostUnload() { /* no-op */ }
}
