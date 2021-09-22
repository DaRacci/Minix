@file:JvmName("PlayerMoveXYZEvent")
package me.racci.raccilib.events

class PlayerMoveXYZEvent(
    val player: org.bukkit.entity.Player, val from: org.bukkit.Location, var to: org.bukkit.Location
    ) : KotlinEvent(true) { }

class PlayerMoveFullXYZEvent(
    val player: org.bukkit.entity.Player, val from: org.bukkit.Location, var to: org.bukkit.Location
    ) : KotlinEvent(true) { }