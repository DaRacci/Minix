package dev.racci.minix.api.collections

import dev.racci.minix.api.exceptions.MissingPluginException
import dev.racci.minix.api.extensions.WithPlugin
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.extensions.player
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PluginService
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerQuitEvent

/**
 * An abstract class for a collection of online players.
 * A player is automatically removed when they disconnect and don't reconnect for more than 30 seconds.
 *
 * @param T The value type of the collection.
 */
abstract class AbstractPlayerCollection<T> : HashMap<Player, T>(), WithPlugin<MinixPlugin> {
    override val plugin: MinixPlugin = PluginService.fromClassloader(this.javaClass.classLoader)
        ?: throw MissingPluginException("Could not find MinixPlugin for classloader ${this.javaClass.classLoader}")

    init {
        event<PlayerQuitEvent>(EventPriority.MONITOR, true) {
            this@AbstractPlayerCollection.remove(player)
        }
    }
}
