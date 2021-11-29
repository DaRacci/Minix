@file:Suppress("UNUSED", "UNCHECKED_CAST")
package me.racci.raccicore.api.lifecycle

import me.racci.raccicore.api.plugin.RacciPlugin

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