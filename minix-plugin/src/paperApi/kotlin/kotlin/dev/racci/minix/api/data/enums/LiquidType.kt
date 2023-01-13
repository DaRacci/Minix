package dev.racci.minix.api.data.enums

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.block.data.Waterlogged
import java.util.concurrent.ConcurrentHashMap

public enum class LiquidType {
    WATER,
    LAVA,
    NON;

    public companion object {
        private val typeCache = ConcurrentHashMap<Material, LiquidType>()

        public fun convert(block: Block): LiquidType = convert(block.state)

        public fun convert(state: BlockState): LiquidType {
            val waterLogged = state.blockData as? Waterlogged ?: return convert(state.type)
            return if (waterLogged.isWaterlogged) WATER else NON
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

        public val BlockState.liquidType: LiquidType get() = convert(this)
        public val Block.liquidType get() = convert(this)
        public val Material.liquidType get() = convert(this)
    }
}
