package dev.racci.minix.api.data.enums

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Waterlogged
import java.util.concurrent.ConcurrentHashMap

public enum class LiquidType {
    WATER,
    LAVA,
    NON;

    public companion object {
        private val typeCache = ConcurrentHashMap<Material, LiquidType>()

        public fun convert(block: Block): LiquidType {
            val waterlogged = block.blockData as? Waterlogged ?: return convert(block.type)
            return if (waterlogged.isWaterlogged) WATER else NON
        }

        public fun convert(type: Material): LiquidType = typeCache.computeIfAbsent(type) {
            when (type) {
                Material.LAVA,
                Material.LAVA_BUCKET -> LAVA
                Material.WATER,
                Material.WATER_BUCKET,
                Material.BUBBLE_COLUMN,
                Material.KELP_PLANT,
                Material.SEAGRASS,
                Material.TALL_SEAGRASS -> WATER
                else -> NON
            }
        }

        public val Block.liquidType: LiquidType get() = convert(this)
        public val Material.liquidType: LiquidType get() = convert(this)
    }
}
