package me.racci.raccilib.events

/**
 * Player enter liquid event
 *
 * @property player
 * @property liquidType
 * @property from
 * @property to
 * @constructor Create empty Player enter liquid event
 */
class PlayerEnterLiquidEvent(
    val player: org.bukkit.entity.Player, val liquidType: Int, val from: org.bukkit.block.Block, val to: org.bukkit.block.Block
    ) : KotlinEvent(true) { }

/**
 * Player exit liquid event
 *
 * @property player
 * @property liquidType
 * @property from
 * @property to
 * @constructor Create empty Player exit liquid event
 */
class PlayerExitLiquidEvent(
    val player: org.bukkit.entity.Player, val liquidType: Int, val from: org.bukkit.block.Block, val to: org.bukkit.block.Block
    ) : KotlinEvent(true) { }