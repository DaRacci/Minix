@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package dev.racci.minix.api.utils.minecraft

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Waterlogged
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval

/**
 * Utilities for Blocks.
 */
public object BlockUtils {

    private fun getNearbyBlocks(
        start: Block,
        materials: List<Material>,
        blocks: ArrayList<Block>,
        limit: Int
    ): ArrayList<Block> {
        for (face in BlockFace.values()) {
            val block = start.getRelative(face)
            if (blocks.contains(block)) continue
            if (materials.contains(block.type)) {
                blocks.add(block)
                if (blocks.size > limit || blocks.size > 2500) return blocks
                blocks.addAll(getNearbyBlocks(block, materials, blocks, limit))
            }
        }
        return blocks
    }

    /**
     * Gets an array list of all blocks in contact with each other.
     *
     * @param start The base block
     * @param materials The blocks to count
     * @param limit The maximum size
     * @return An arraylist of the veins blocks
     */
    public fun getVein(
        start: Block,
        materials: List<Material>,
        limit: Int
    ): ArrayList<Block> = getNearbyBlocks(start, materials, ArrayList(), limit)

    @ScheduledForRemoval(inVersion = "4.5.0")
    @Deprecated("Use BlockFace#getOppositeFace instead", ReplaceWith("face.oppositeFace", "org.bukkit.block.BlockFace"))
    public fun getOpposite(face: BlockFace): BlockFace = when (face) {
        BlockFace.UP -> BlockFace.DOWN
        BlockFace.DOWN -> BlockFace.UP
        BlockFace.SOUTH -> BlockFace.NORTH
        BlockFace.NORTH -> BlockFace.SOUTH
        BlockFace.EAST -> BlockFace.WEST
        BlockFace.WEST -> BlockFace.EAST
        BlockFace.SOUTH_EAST -> BlockFace.NORTH_WEST
        BlockFace.SOUTH_WEST -> BlockFace.NORTH_EAST
        BlockFace.SOUTH_SOUTH_EAST -> BlockFace.NORTH_NORTH_WEST
        BlockFace.SOUTH_SOUTH_WEST -> BlockFace.NORTH_NORTH_EAST
        BlockFace.NORTH_EAST -> BlockFace.SOUTH_WEST
        BlockFace.NORTH_WEST -> BlockFace.SOUTH_EAST
        BlockFace.NORTH_NORTH_EAST -> BlockFace.SOUTH_SOUTH_WEST
        BlockFace.NORTH_NORTH_WEST -> BlockFace.SOUTH_SOUTH_EAST
        BlockFace.EAST_NORTH_EAST -> BlockFace.WEST_SOUTH_WEST
        BlockFace.EAST_SOUTH_EAST -> BlockFace.WEST_NORTH_WEST
        BlockFace.WEST_NORTH_WEST -> BlockFace.EAST_SOUTH_EAST
        BlockFace.WEST_SOUTH_WEST -> BlockFace.EAST_NORTH_EAST
        BlockFace.SELF -> BlockFace.SELF
    }

    /**
     * Gets the block another block (e.g. a ladder) is attached to.
     *
     * @param directional Block to check
     * @return Block that supports the block to check
     */
    public fun getSupportingBlock(directional: Block): Block {
        if (directional.blockData is Directional) {
            return directional.getRelative((directional.blockData as Directional).facing.oppositeFace)
        }
        throw IllegalArgumentException("Provided Block's BlockData is not an instance of Directional")
    }

    /**
     * Gets the BlockFace of the existing block that must have been right-clicked to place the new Block.
     *
     * @param existing Existing block
     * @param newBlock New block
     * @return Existing block's BlockFace that must have been right-clicked to place the new block
     */
    public fun getPlacedAgainstFace(
        existing: Block,
        newBlock: Block?
    ): BlockFace {
        for (blockFace in BlockFace.values()) {
            if (existing.getRelative(blockFace) == newBlock) return blockFace
        }
        throw IllegalArgumentException("No BlockFace found")
    }

    /**
     * Returns the value of if the block is a liquid.
     * 0 = Not a liquid
     * 1 = Water or waterlogged
     * 2 = Lava
     *
     * @param block The target block
     * @return The state of the block
     */
    public fun isLiquid(block: Block): Int {
        val blockData = block.blockData
        return when (block.type) {
            Material.WATER -> 1
            Material.LAVA -> 2
            else -> if (blockData is Waterlogged && blockData.isWaterlogged) 1 else 0
        }
    }
}
