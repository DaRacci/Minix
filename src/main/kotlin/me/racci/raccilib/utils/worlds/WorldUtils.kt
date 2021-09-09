@file:Suppress("unused")
@file:JvmName("WorldUtils")
package me.racci.raccilib.utils.worlds

import org.bukkit.entity.Player

object WorldTime {

    fun isDay(player: Player): Boolean {
        val time = player.world.time
        return time < 13400 || time > 23400
    }

    fun isNight(player: Player): Boolean {
        return !isDay(player)
    }


}