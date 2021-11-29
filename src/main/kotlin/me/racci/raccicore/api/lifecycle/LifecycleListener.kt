package me.racci.raccicore.api.lifecycle

import me.racci.raccicore.api.extensions.WithPlugin
import me.racci.raccicore.api.plugin.RacciPlugin

interface LifecycleListener<P: RacciPlugin>: PluginLifecycle, WithPlugin<P> {

    /**
     * Handles invoking the [Lifecycle]
     */
    override suspend fun invoke(event: LifecycleEvent) {
        when(event) {
            LifecycleEvent.LOAD    -> onLoad()
            LifecycleEvent.ENABLE  -> onEnable()
            LifecycleEvent.RELOAD  -> onReload()
            LifecycleEvent.DISABLE -> onDisable()
        }
    }

    /**
     * Called when the [RacciPlugin] begins early loading.
     */
    suspend fun onLoad() {}

    /**
     * Called when the [RacciPlugin] is enabled by the server and ready.
     */
    suspend fun onEnable() {}

    /**
     * Called when reloading the [RacciPlugin].
     */
    suspend fun onReload() {}

    /**
     * Called when the plugin is disabled.
     */
    suspend fun onDisable() {}


}