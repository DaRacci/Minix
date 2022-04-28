package dev.racci.minix.api.utils.minecraft

import dev.racci.minix.api.utils.UtilObject
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

object WorldUtils : UtilObject by UtilObject {

    fun isDay(world: World): Boolean = with(world.time) { this < 13400 || this > 23400 }

    fun isDay(player: Player): Boolean = isDay(player.world)

    fun isDay(location: Location): Boolean = isDay(location.world)

    fun isNight(player: Player): Boolean = !isDay(player)

    fun isNight(world: World): Boolean = !isDay(world)

    fun isNight(location: Location): Boolean = !isDay(location)

    fun isOverworld(world: World): Boolean = world.environment == World.Environment.NORMAL

    fun isNether(world: World): Boolean = world.environment == World.Environment.NETHER

    fun isEnd(world: World): Boolean = world.environment == World.Environment.THE_END
}
