package dev.racci.minix.core

import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.core.services.IntegrationService
import kotlinx.coroutines.runBlocking
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerLoadEvent

/**
 * Handles listening for the initial server load event try early load any integrations.
 * Listener is unregistered after the event is received.
 */
internal class LoadListener : Listener {

    @EventHandler
    fun onLoad(event: ServerLoadEvent) {
        HandlerList.unregisterAll(this)
        runBlocking { getKoin().get<IntegrationService>().serverLoad() }
    }
}
