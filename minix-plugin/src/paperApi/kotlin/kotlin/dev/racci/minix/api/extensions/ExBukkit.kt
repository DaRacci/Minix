@file:Suppress("Unused")

package dev.racci.minix.api.extensions

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginManager

/**
 * Alias for [Bukkit.getServer]
 */
public inline val server: Server get() = Bukkit.getServer()

/**
 * Alias for [Bukkit.getPluginManager]
 */
public inline val pm: PluginManager get() = Bukkit.getPluginManager()

/**
 * Broadcast a component to the server.
 *
 * @param component The component to broadcast
 */
public fun broadcast(component: Component): Int = server.broadcast(component)

/**
 * Broadcast a component only some players.
 *
 * @param players The players to broadcast to.
 * @param component The Component to broadcast.
 * @receiver
 */
public fun broadcast(
    players: Iterable<Player> = onlinePlayers,
    component: Player.() -> Component
): Unit = players.forEach(component::invoke)

/**
 * Broadcast a component to the players
 * of this [Collection].
 *
 * @param component The component to broadcast
 */
public fun Collection<Player>.broadcast(
    component: Player.() -> Component
): Unit = broadcast(this, component)

/**
 * Broadcast a component to the players
 * of this [Array].
 *
 * @param component The component to broadcast
 */
public fun Array<Player>.broadcast(
    component: Player.() -> Component
): Unit = broadcast(this.toList(), component)

/**
 * An Object that represents the [ConsoleCommandSender]
 * with additional functions.
 */
public object Console : ConsoleCommandSender by Bukkit.getConsoleSender() {

    /**
     * Send a Command as the [ConsoleCommandSender].
     *
     * @param command The command to run.
     */
    public fun command(
        command: String
    ): Boolean = Bukkit.dispatchCommand(this, command)
}
