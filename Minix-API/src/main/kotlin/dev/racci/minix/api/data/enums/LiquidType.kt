package dev.racci.minix.api.data.enums

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Waterlogged
import java.util.concurrent.ConcurrentHashMap

enum class LiquidType {
    WATER,
    LAVA,
    NON;

    companion object {
        private val typeCache = ConcurrentHashMap<Material, LiquidType>()

        fun convert(block: Block): LiquidType {
            val waterlogged = block.blockData as? Waterlogged ?: return convert(block.type)
            return if (waterlogged.isWaterlogged) WATER else NON
        }

        fun convert(type: Material) = typeCache.computeIfAbsent(type) {
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

        val Block.liquidType get() = convert(this)
        val Material.liquidType get() = convert(this)
    }
}
