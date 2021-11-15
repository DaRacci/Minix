package me.racci.raccicore.lifecycle

import me.racci.raccicore.RacciPlugin

object LifecycleUtils {

    inline fun <reified T : PluginLifecycle> RacciPlugin.getOrInsertGenericLifecycle(
        priority: Int,
        factory: () -> T
    ) = lifecycleListeners
            .map{it.listener}
            .filterIsInstance<T>()
            .firstOrNull()
        ?: factory().also {lifecycleListeners.add(Lifecycle(priority, it))}

}