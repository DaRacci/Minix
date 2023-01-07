package dev.racci.minix.integrations.misc

import dev.racci.minix.api.data.Priority
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.koin
import dev.racci.minix.flowbus.receiver.EventReceiver
import dev.racci.minix.flowbus.subscribeFlow
import dev.racci.minix.integrations.Integration
import dev.racci.minix.integrations.annotations.MappedIntegration
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import net.frankheijden.serverutils.bukkit.events.BukkitPluginUnloadEvent
import net.frankheijden.serverutils.common.events.PluginEvent

@MappedIntegration("ServerUtils")
public abstract class ServerUtilsIntegration : Integration, EventReceiver by getKoin().get() {
    public override suspend fun handleEnable() {
        coroutineScope {
            subscribeFlow<BukkitPluginUnloadEvent>(Priority.MONITOR)
                .filter { event -> event.stage == PluginEvent.Stage.PRE }
                .mapNotNull { event -> event.plugin as? MinixPlugin }
                .collect(koin.get<PluginService>()::unloadPlugin)
        }
    }
}
