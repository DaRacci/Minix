package dev.racci.minix.api.utils.minecraft

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

object WorldUtils {

    fun isDay(world: World): Boolean {
        val time = world.time
        return time < 13400 || time > 23400
    }

    fun isDay(player: Player): Boolean = isDay(player.world)

    fun isDay(location: Location): Boolean = isDay(location.world)

    fun isNight(player: Player): Boolean = !isDay(player)

    fun isNight(world: World): Boolean = !isDay(world)

    fun isNight(location: Location): Boolean = !isDay(location)

    fun isOverworld(world: World): Boolean = world.environment == World.Environment.NORMAL

    fun isNether(world: World): Boolean = world.environment == World.Environment.NETHER

    fun isEnd(world: World): Boolean = world.environment == World.Environment.THE_END
}
