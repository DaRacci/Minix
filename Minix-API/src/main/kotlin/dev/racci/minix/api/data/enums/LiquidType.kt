package dev.racci.minix.api.data.enums

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Waterlogged

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
