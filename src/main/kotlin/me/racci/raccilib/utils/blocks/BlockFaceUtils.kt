@file:JvmName("BlockFaceUtils")
package me.racci.raccilib.utils.blocks

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.block.BlockFace.*

/**
 * Returns the opposite BlockFace for a given BlockFace. E.g. EAST_NORTH_EAST will return WEST_SOUTH_WEST. SELF will return SELF.
 *
 * @param face Original BlockFace
 * @return Opposite BlockFace
 */
fun getOpposite(face: BlockFace?): BlockFace {
    when (face) {
        UP -> return DOWN
        DOWN -> return UP
        SOUTH -> return NORTH
        NORTH -> return SOUTH
        EAST -> return WEST
        WEST -> return EAST
        SOUTH_EAST -> return NORTH_WEST
        SOUTH_WEST -> return NORTH_EAST
        SOUTH_SOUTH_EAST -> return NORTH_NORTH_WEST
        SOUTH_SOUTH_WEST -> return NORTH_NORTH_EAST
        NORTH_EAST -> return SOUTH_WEST
        NORTH_WEST -> return SOUTH_EAST
        NORTH_NORTH_EAST -> return SOUTH_SOUTH_WEST
        NORTH_NORTH_WEST -> return SOUTH_SOUTH_EAST
        EAST_NORTH_EAST -> return WEST_SOUTH_WEST
        EAST_SOUTH_EAST -> return WEST_NORTH_WEST
        WEST_NORTH_WEST -> return EAST_SOUTH_EAST
        WEST_SOUTH_WEST -> return EAST_NORTH_EAST
        SELF -> return SELF
    }
    throw IllegalArgumentException()
}

/**
 * Gets the block another block (e.g. a ladder) is attached to
 *
 * @param directional Block to check
 * @return Block that supports the block to check
 */
fun getSupportingBlock(directional: Block): Block {
    if (directional.blockData is Directional) {
        return directional.getRelative(getOpposite((directional.blockData as Directional).facing))
    }
    throw IllegalArgumentException("Provided Block's BlockData is not an instance of Directional")
}

/**
 * Gets the BlockFace of the existing block that must have been right-clicked to place the new Block
 *
 * @param existing Existing block
 * @param newBlock New block
 * @return Existing block's BlockFace that must have been right-clicked to place the new block
 */
fun getPlacedAgainstFace(existing: Block, newBlock: Block?): BlockFace {
    for (blockFace in BlockFace.values()) {
        if (existing.getRelative(blockFace) == newBlock) return blockFace
    }
    throw IllegalArgumentException("No BlockFace found")
}