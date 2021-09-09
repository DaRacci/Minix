@file:Suppress("unused")
@file:JvmName("BlockTypeUtils")
package me.racci.raccilib.utils.blocks

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Waterlogged

fun isLiquid(block: Block): Int {
    return when(block.type) {
        Material.WATER -> 1
        Material.LAVA -> 2
        else -> if((block.blockData as Waterlogged).isWaterlogged) 1 else 0
    }
}