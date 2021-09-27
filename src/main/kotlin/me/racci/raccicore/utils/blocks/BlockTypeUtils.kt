package me.racci.raccicore.utils.blocks

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Waterlogged

fun isLiquid(block: Block): Int {
    val blockData = block.blockData
    return when(block.type) {
        Material.WATER -> 1
        Material.LAVA -> 2
        else -> if((blockData is Waterlogged) && blockData.isWaterlogged) 1 else 0
    }
}