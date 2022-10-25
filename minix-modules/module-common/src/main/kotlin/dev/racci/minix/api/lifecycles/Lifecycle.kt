package dev.racci.minix.api.lifecycles

public interface Lifecycle {

    /** Called when this object is created|loaded */
    public suspend fun handleLoad() { /* no-op */ }

    /** Called when this object is destroyed|disposed|unloaded */
    public suspend fun handleUnload() { /* no-op */ }
}
