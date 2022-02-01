@file:Suppress("UNUSED")

package dev.racci.minix.api.extensions

import dev.racci.minix.api.annotations.MinixDsl
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.jetbrains.annotations.ApiStatus

/**
 * Send a block change to the [players]
 * This block change is only visual and has
 * no actual effect on the block.
 *
 * @param material The new [Material].
 * @param blockData Block data dsl for modifying the new block.
 * @param players The players to see this change.
 */

@MinixDsl
@ApiStatus.AvailableSince("1.0.0")
fun Block.sendBlockChange(
    material: Material,
    blockData: BlockData.() -> Unit,
    vararg players: Player,
) {
    players.filter { it.world.name == world.name }.forEach {
        it.sendBlockChange(location, material.createBlockData(blockData))
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

@ApiStatus.AvailableSince("1.0.0")
fun Block.sendBlockChange(
    blockData: BlockData,
    vararg players: Player
) {
    players.filter { it.world.name == world.name }.forEach {
        it.sendBlockChange(location, blockData)
    }
}
