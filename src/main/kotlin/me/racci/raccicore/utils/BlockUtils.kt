package me.racci.raccicore.utils

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Waterlogged

object BlockUtils {

    /**
     * Returns the opposite BlockFace for a given BlockFace. E.g. EAST_NORTH_EAST will return WEST_SOUTH_WEST. SELF will return SELF.
     *
     * @param face Original BlockFace
     * @return Opposite BlockFace
     */
    fun getOpposite(face: BlockFace): BlockFace {
        when (face) {
            BlockFace.UP               -> return BlockFace.DOWN
            BlockFace.DOWN             -> return BlockFace.UP
            BlockFace.SOUTH            -> return BlockFace.NORTH
            BlockFace.NORTH            -> return BlockFace.SOUTH
            BlockFace.EAST             -> return BlockFace.WEST
            BlockFace.WEST             -> return BlockFace.EAST
            BlockFace.SOUTH_EAST       -> return BlockFace.NORTH_WEST
            BlockFace.SOUTH_WEST       -> return BlockFace.NORTH_EAST
            BlockFace.SOUTH_SOUTH_EAST -> return BlockFace.NORTH_NORTH_WEST
            BlockFace.SOUTH_SOUTH_WEST -> return BlockFace.NORTH_NORTH_EAST
            BlockFace.NORTH_EAST       -> return BlockFace.SOUTH_WEST
            BlockFace.NORTH_WEST       -> return BlockFace.SOUTH_EAST
            BlockFace.NORTH_NORTH_EAST -> return BlockFace.SOUTH_SOUTH_WEST
            BlockFace.NORTH_NORTH_WEST -> return BlockFace.SOUTH_SOUTH_EAST
            BlockFace.EAST_NORTH_EAST  -> return BlockFace.WEST_SOUTH_WEST
            BlockFace.EAST_SOUTH_EAST  -> return BlockFace.WEST_NORTH_WEST
            BlockFace.WEST_NORTH_WEST  -> return BlockFace.EAST_SOUTH_EAST
            BlockFace.WEST_SOUTH_WEST  -> return BlockFace.EAST_NORTH_EAST
            BlockFace.SELF             -> return BlockFace.SELF
        }
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

    fun isLiquid(block: Block): Int {
        val blockData = block.blockData
        return when(block.type) {
            Material.WATER -> 1
            Material.LAVA  -> 2
            else           -> if((blockData is Waterlogged) && blockData.isWaterlogged) 1 else 0
        }
    }


}