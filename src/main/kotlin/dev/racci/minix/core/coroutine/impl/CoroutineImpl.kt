package dev.racci.minix.core.coroutine.impl

import dev.racci.minix.api.coroutine.contract.Coroutine
import dev.racci.minix.api.coroutine.contract.CoroutineSession
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.core.coroutine.listener.PluginListener
import dev.racci.minix.core.coroutine.service.CoroutineSessionImpl

class CoroutineImpl : Coroutine {

    private val items = HashMap<MinixPlugin, CoroutineSession>()

    /**
     * Get coroutine session for the given plugin.
     */
    override fun getCoroutineSession(plugin: MinixPlugin): CoroutineSession {
        if (!items.containsKey(plugin)) {
            startCoroutineSession(plugin)
        }

        return items[plugin]!!
    }

    /**
     * Disables coroutine for the given plugin.
     */
    override fun disable(plugin: MinixPlugin) {
        if (!items.containsKey(plugin)) return

        val session = items[plugin]!!
        session.dispose()
        items.remove(plugin)
    }

    private fun startCoroutineSession(plugin: MinixPlugin) {
        if (!plugin.isEnabled) {
            plugin.log.throwing(
                RuntimeException(
                    "Plugin ${plugin.name} attempt to start a new coroutine session while being disabled. " +
                        "Dispatchers such as plugin.minecraftDispatcher and plugin.asyncDispatcher are already " +
                        "disposed at this point and cannot be used!"
                )
            )
        }

        val pluginListener = PluginListener(this, plugin)
        items[plugin] = CoroutineSessionImpl(plugin)
        plugin.server.pluginManager.registerEvents(pluginListener, plugin)
    }
}
