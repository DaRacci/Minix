@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package dev.racci.minix.api.events

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Waterlogged
import org.bukkit.entity.Player

sealed class PlayerLiquidEvent(
    player: Player,
    val previousType: LiquidType,
    val newType: LiquidType
) : KPlayerEvent(player, true) {

    operator fun component2(): LiquidType = previousType
    operator fun component3(): LiquidType = newType
}

class PlayerEnterLiquidEvent(
    player: Player,
    previousType: LiquidType,
    newType: LiquidType
) : PlayerLiquidEvent(player, previousType, newType)

class PlayerExitLiquidEvent(
    player: Player,
    previousType: LiquidType,
    newType: LiquidType
) : PlayerLiquidEvent(player, previousType, newType)

enum class LiquidType {
    WATER,
    LAVA,
    NON;

    companion object {

        fun convert(block: Block) = when (block.type) {
            Material.WATER -> WATER
            Material.LAVA -> LAVA
            else -> if (block is Waterlogged && block.isWaterlogged) WATER else NON
        }

        fun convert(bucket: Material) = when (bucket) {
            Material.WATER_BUCKET -> WATER
            Material.LAVA_BUCKET -> LAVA
            else -> NON
        }

        val Block.liquidType get() = convert(this)
        val Material.liquidType get() = convert(this)
    }
}