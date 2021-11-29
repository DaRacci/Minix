@file:Suppress("UNUSED")
package me.racci.raccicore.api.extensions

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

/**
 * Send a block change to the [players]
 * This block change is only visual and has
 * no actual effect on the block.
 *
 * @param material The new [Material].
 * @param data Extra data to apply to the [Block].
 * @param players The players to see this change.
 */
fun Block.sendBlockChange(
    material: Material,
    data: Byte = 0,
    vararg players: Player
) {
    players.filter{it.world.name == world.name}.forEach {
        // TODO Find non deprecated
        it.sendBlockChange(location, material, data)
    }
}

/**
 * Send a block change to the [players]
 * This block change is only visual and has
 * no actual effect on the block.
 *
 * @param blockData The new [BlockData].
 * @param players The players to see this change.
 */
fun Block.sendBlockChange(
    blockData: BlockData,
    vararg players: Player
) {
    players.filter{it.world.name == world.name}.forEach {
        it.sendBlockChange(location, blockData)
    }
}