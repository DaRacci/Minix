package dev.racci.minix.core

import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.core.services.IntegrationService
import kotlinx.coroutines.runBlocking
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerLoadEvent

public class LoadListener : Listener {

    @EventHandler
    public fun onLoad(event: ServerLoadEvent) {
        HandlerList.unregisterAll(this)
        runBlocking { getKoin().get<IntegrationService>().serverLoad() }
    }
}
