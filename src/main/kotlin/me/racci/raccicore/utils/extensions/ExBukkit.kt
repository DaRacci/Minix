package me.racci.raccicore.utils.extensions

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

val server: Server get() = Bukkit.getServer()

object Console : ConsoleCommandSender by Bukkit.getConsoleSender() {
    fun command(command: String) = Bukkit.dispatchCommand(this, command)
}

fun broadcast(component: Component) {
    Bukkit.getServer().broadcast(component)
}

fun broadcast(
    players: Iterable<Player> = Bukkit.getOnlinePlayers(),
    component: Player.() -> Component
) {
    players.forEach(component::invoke)
}

fun Collection<Player>.broadcast(
    component: Player.() -> Component
) = broadcast(this, component)

fun Array<Player>.broadcast(
    component: Player.() -> Component
) = broadcast(this.toList(), component)