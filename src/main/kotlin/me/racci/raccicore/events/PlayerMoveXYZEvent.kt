package me.racci.raccicore.events

/**
 * Player move x y z event
 *
 * @property player
 * @property from
 * @property to
 * @constructor Create empty Player move x y z event
 */
class PlayerMoveXYZEvent(
    val player: org.bukkit.entity.Player, val from: org.bukkit.Location, var to: org.bukkit.Location
    ) : KotlinEvent(true) { }

/**
 * Player move full x y z event
 *
 * @property player
 * @property from
 * @property to
 * @constructor Create empty Player move full x y z event
 */
class PlayerMoveFullXYZEvent(
    val player: org.bukkit.entity.Player, val from: org.bukkit.Location, var to: org.bukkit.Location
    ) : KotlinEvent(true) { }