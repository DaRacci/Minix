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
 * @param dataMutator Block data dsl for modifying the new block.
 * @param players The players to see this change.
 */
@MinixDsl
public fun Block.sendBlockChange(
    material: Material,
    dataMutator: BlockData.() -> Unit,
    vararg players: Player
): Unit = this.sendBlockChange(material.createBlockData(dataMutator), *players)

/**
 * Send a block change to the [players]
 * This block change is only visual and has
 * no actual effect on the block.
 *
 * @param blockData The new [BlockData].
 * @param players The players to see this change.
 */
@MinixDsl
public fun Block.sendBlockChange(
    blockData: BlockData,
    vararg players: Player
): Unit = players.filter { it.world.name == world.name }
    .forEach { it.sendBlockChange(location, blockData) }
