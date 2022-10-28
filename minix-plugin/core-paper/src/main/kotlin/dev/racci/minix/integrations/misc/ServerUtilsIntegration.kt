package dev.racci.minix.integrations.misc

import dev.racci.minix.api.extensions.reflection.safeCast
import dev.racci.minix.api.logger.MinixLoggerFactory
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.flowbus.receiver.EventReceiver
import dev.racci.minix.flowbus.subscribe
import dev.racci.minix.integrations.Integration
import dev.racci.minix.integrations.annotations.MappedIntegration
import net.frankheijden.serverutils.bukkit.events.BukkitPluginUnloadEvent
import net.frankheijden.serverutils.common.events.PluginEvent

@MappedIntegration("ServerUtils")
public abstract class ServerUtilsIntegration : Integration, EventReceiver by getKoin().get() {
    private val logger by MinixLoggerFactory

    public override suspend fun handleEnable() {
        subscribe<BukkitPluginUnloadEvent> {
            if (stage == PluginEvent.Stage.POST) {
                logger.debug { "Ignoring post disable event for ${plugin.name}" }
                return@subscribe
            }

            logger.debug { "Handling pre unload event for ${plugin.name}" }

            val plugin = plugin.safeCast<MinixPlugin>() ?: return@subscribe
            PluginService.unloadPlugin(plugin)
        }
    }
}
