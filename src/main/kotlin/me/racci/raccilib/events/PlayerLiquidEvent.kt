@file:JvmName("PlayerLiquidEvent")
package me.racci.raccilib.events

class PlayerEnterLiquidEvent(
    val player: org.bukkit.entity.Player, val liquidType: Int, val from: org.bukkit.block.Block, val to: org.bukkit.block.Block
    ) : KotlinEvent(true) { }

class PlayerExitLiquidEvent(
    val player: org.bukkit.entity.Player, val liquidType: Int, val from: org.bukkit.block.Block, val to: org.bukkit.block.Block
    ) : KotlinEvent(true) { }