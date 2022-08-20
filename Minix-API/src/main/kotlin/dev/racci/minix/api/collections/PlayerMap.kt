package dev.racci.minix.api.collections

import dev.racci.minix.api.exceptions.MissingPluginException
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.scheduler
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerQuitEvent
import kotlin.time.Duration.Companion.seconds

/** A map, which removes a player once they are offline for more than 30 seconds. */
class PlayerMap<T> : HashMap<Player, T>(), WithPlugin<MinixPlugin> {
    override val plugin: MinixPlugin = PluginService.firstNonMinixPlugin() ?: throw MissingPluginException("Could not find MinixPlugin in the stack.")

    init {
        event<PlayerQuitEvent>(EventPriority.MONITOR, true) {
            scheduler {
                if (player.isOnline) return@scheduler
                this@PlayerMap.remove(player)
            }.runAsyncTaskLater(plugin, 30.seconds)
        }
    }
}
