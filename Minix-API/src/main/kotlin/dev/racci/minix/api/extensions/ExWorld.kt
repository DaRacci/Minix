@file:Suppress("UNUSED")
package dev.racci.minix.api.extensions

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

val Player.isDay: Boolean get() = world.isDayTime

val Location.isDay: Boolean get() = world.isDayTime

val Player.isNight: Boolean get() = !isDay

val World.isNight: Boolean get() = !isDayTime

val Location.isNight: Boolean get() = !isDay

val World.isOverworld: Boolean get() = environment == World.Environment.NORMAL

val World.isNether: Boolean get() = environment == World.Environment.NETHER

val World.isEnd: Boolean get() = environment == World.Environment.THE_END
