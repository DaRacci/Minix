package dev.racci.minix.integrations.misc

import dev.racci.minix.integrations.Integration
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.safeCast
import dev.racci.minix.integrations.annotations.IntegrationTarget
import net.frankheijden.serverutils.bukkit.events.BukkitPluginDisableEvent
import net.frankheijden.serverutils.common.events.PluginEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

@IntegrationTarget("ServerUtils")
public abstract class ServerUtilsIntegration : Integration {
    public override suspend fun handleEnable() {
        logger.info { "ServerUtilsIntegration loaded!" }

        registerEvents(object : Listener {
            @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
            fun onPluginUnload(event: BukkitPluginDisableEvent) {
                if (event.stage == PluginEvent.Stage.POST) return

                val minixPlugin = event.plugin.safeCast<MinixPlugin>() ?: return
                PluginService[minixPlugin].wantsFullUnload = true
            }
        })
    }
}
