package dev.racci.minix.core.integrations

import dev.racci.minix.api.annotations.MappedIntegration
import dev.racci.minix.api.annotations.MinixInternal
import dev.racci.minix.api.integrations.Integration
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import dev.racci.minix.api.utils.safeCast
import net.frankheijden.serverutils.bukkit.events.BukkitPluginDisableEvent
import net.frankheijden.serverutils.common.events.PluginEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

@MappedIntegration(
    "ServerUtils",
    Minix::class
)
@OptIn(MinixInternal::class)
class ServerUtilsIntegration(override val plugin: MinixPlugin) : Integration {
    override suspend fun handleEnable() {
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
