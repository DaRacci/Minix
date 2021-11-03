package me.racci.raccicore.events

import org.bukkit.Location

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
    val player: org.bukkit.entity.Player, val liquidType: Int, val from: Location, val to: Location
    ) : KotlinEvent(true)

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
    val player: org.bukkit.entity.Player, val liquidType: Int, val from: Location, val to: Location
    ) : KotlinEvent(true)