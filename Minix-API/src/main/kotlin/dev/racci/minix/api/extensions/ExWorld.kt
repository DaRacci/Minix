@file:Suppress("UNUSED")
package dev.racci.minix.api.extensions

import org.bukkit.World

val World.isNight: Boolean get() = !isDayTime

val World.isOverworld: Boolean get() = environment == World.Environment.NORMAL

val World.isNether: Boolean get() = environment == World.Environment.NETHER

val World.isEnd: Boolean get() = environment == World.Environment.THE_END
