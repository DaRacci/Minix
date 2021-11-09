package me.racci.raccicore.events

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.Waterlogged
import org.bukkit.entity.Player

/**
 * This event is fires when the player enters water, lava or a waterlogged block.
 *
 * ## This method is Fired Asynchronously
 *
 * @property from The previous block where the player was before entering the liquid.
 * @property to The liquid block the player has entered.
 *
 * @param player The player of the event.
 * @param liquidType [Material.WATER] if it was water or a [Waterlogged] block,
 * [Material.LAVA] if it was Lava
 */
class PlayerEnterLiquidEvent(
    player: Player,
    liquidType: Int,
    val from: Location,
    val to: Location
): KPlayerEvent(player, true) {

    val liquidType: Material =
            if(liquidType == 1) {
                Material.WATER
            } else Material.LAVA

}

/**
 * This event is fires when the player exits water, lava or a waterlogged block.
 *
 * ## This method is Fired Asynchronously
 *
 * @property from The liquid block that the player has exited.
 * @property to The new non-liquid block.
 *
 * @param player The player of the event.
 * @param liquidType [Material.WATER] if it was water or a [Waterlogged] block,
 * [Material.LAVA] if it was Lava
 */
class PlayerExitLiquidEvent(
    player: Player,
    liquidType: Int,
    val from: Location,
    val to: Location
): KPlayerEvent(player, true) {

    val liquidType: Material =
            if(liquidType == 1) {
                Material.WATER
            } else Material.LAVA

}