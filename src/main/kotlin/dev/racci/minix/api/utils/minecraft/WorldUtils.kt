@file:Suppress("UNUSED")

package dev.racci.minix.api.utils.minecraft

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

object WorldUtils {

    fun isDay(player: Player): Boolean {
        val time = player.world.time
        return time < 13400 || time > 23400
    }

    fun isDay(world: World): Boolean {
        val time = world.time
        return time < 13400 || time > 23400
    }

    fun isDay(location: Location): Boolean {
        val time = location.world.time
        return time < 13400 || time > 23400
    }

    fun isNight(player: Player): Boolean {
        return !isDay(player)
    }

    fun isNight(world: World): Boolean {
        return !isDay(world)
    }

    fun isNight(location: Location): Boolean {
        return !isDay(location)
    }
}
