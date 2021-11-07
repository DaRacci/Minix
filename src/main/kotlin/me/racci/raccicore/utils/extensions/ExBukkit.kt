package me.racci.raccicore.utils.extensions

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player

val server  get() = Bukkit.getServer()
val console get() = Bukkit.getConsoleSender()
val pm      get() = Bukkit.getPluginManager()

fun broadcast(component: Component) =
    server.broadcast(component)

fun broadcast(
    players: Iterable<Player> = onlinePlayers,
    component: Player.() -> Component
) = players.forEach(component::invoke)

fun Collection<Player>.broadcast(
    component: Player.() -> Component
) = broadcast(this, component)

fun Array<Player>.broadcast(
    component: Player.() -> Component
) = broadcast(this.toList(), component)