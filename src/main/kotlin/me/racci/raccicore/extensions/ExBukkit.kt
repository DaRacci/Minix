package me.racci.raccicore.extensions

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

/**
 * An Object that represents the [ConsoleCommandSender]
 * with additional functions.
 */
object Console: ConsoleCommandSender by Bukkit.getConsoleSender() {

    /**
     * Send a Command as the [ConsoleCommandSender]
     *
     * @param command The command to run.
     */
    fun command(
        command: String
    ) = Bukkit.dispatchCommand(this, command)
}

/**
 * Alias for [Bukkit.getServer]
 */
val server get() = Bukkit.getServer()

/**
 * Alias for [Bukkit.getPluginManager]
 */
val pm get() = Bukkit.getPluginManager()

/**
 * Broadcast a component to the server.
 *
 * @param component The component to broadcast
 */
fun broadcast(component: Component) =
    server.broadcast(component)

/**
 * Broadcast a component only some players
 *
 * @param players The players to broadcast to.
 * @param component The Component to broadcast.
 * @receiver
 */
fun broadcast(
    players: Iterable<Player> = onlinePlayers,
    component: Player.() -> Component
) = players.forEach(component::invoke)

/**
 * Broadcast a component to the players
 * of this [Collection]
 *
 * @param component The component to broadcast
 */
fun Collection<Player>.broadcast(
    component: Player.() -> Component
) = broadcast(this, component)

/**
 * Broadcast a component to the players
 * of this [Array]
 *
 * @param component The component to broadcast
 */
fun Array<Player>.broadcast(
    component: Player.() -> Component
) = broadcast(this.toList(), component)


@Deprecated("Using object for additional features now", ReplaceWith("Console"))
val console get() = Bukkit.getConsoleSender()