package dev.racci.minix.core.coroutine.impl

import dev.racci.minix.api.coroutine.contract.CoroutineService
import dev.racci.minix.api.coroutine.contract.CoroutineSession
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import org.koin.core.component.inject

class CoroutineServiceImpl : CoroutineService {
    private val pluginService by inject<PluginService>()

    override fun getCoroutineSession(plugin: MinixPlugin): CoroutineSession = pluginService.coroutineSession[plugin]

    override fun disable(plugin: MinixPlugin) {
        val session = pluginService.coroutineSession.getIfPresent(plugin) ?: return
        session.dispose()
        pluginService.coroutineSession.invalidate(plugin)
    }
}
