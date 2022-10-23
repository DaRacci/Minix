package dev.racci.minix.api.extensions // ktlint-disable filename

import dev.racci.minix.api.annotations.MinixDsl
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
 * @param blockDataUnit Block data dsl for modifying the new block.
 * @param players The players to see this change.
 */
@MinixDsl
public fun Block.sendBlockChange(
    material: Material,
    blockDataUnit: BlockData.() -> Unit,
    vararg players: Player
) {
    val blockData = material.createBlockData(blockDataUnit)
    for (player in players) {
        if (player.world.name != world.name) continue
        player.sendBlockChange(location, blockData)
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
public fun Block.sendBlockChange(
    blockData: BlockData,
    vararg players: Player
) {
    for (player in players) {
        if (player.world.name != world.name) continue
        player.sendBlockChange(location, blockData)
    }
}
