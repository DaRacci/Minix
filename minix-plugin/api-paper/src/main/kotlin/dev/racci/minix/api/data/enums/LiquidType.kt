package dev.racci.minix.api.data.enums

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Waterlogged

public enum class LiquidType {
    WATER,
    LAVA,
    NON;

    public companion object {

        public fun convert(block: Block): LiquidType = when (block.type) {
            Material.WATER -> WATER
            Material.LAVA -> LAVA
            else -> if (block is Waterlogged && block.isWaterlogged) WATER else NON
        }

        public fun convert(bucket: Material): LiquidType = when (bucket) {
            Material.WATER_BUCKET -> WATER
            Material.LAVA_BUCKET -> LAVA
            else -> NON
        }

        public val Block.liquidType: LiquidType get() = convert(this)
        public val Material.liquidType: LiquidType get() = convert(this)
    }
}
