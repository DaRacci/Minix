package dev.racci.minix.api.events.keybind

import dev.racci.minix.api.events.player.KPlayerEvent
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Base class for all combo events.
 *
 * @param player The player who triggered the event.
 * @param item The item present in the players main hand during the event.
 * @param blockData The block data present at the location of the event.
 * @param entity The entity present at the location of the event.
 */
sealed class ComboEvent(
    player: Player,
    val item: ItemStack?,
    val blockData: BlockData? = null,
    val entity: Entity? = null
) : KPlayerEvent(player, true) {

    /**
     * If true, this means the used item in the event is not a players fists.
     */
    open val hasItem get() = item != null

    /**
     * Returns if this event had an entity Involved.
     */
    val isEntityEvent get() = entity != null

    /**
     * Returns if this event had a block involved.
     */
    val isBlockEvent get() = blockData != null

    operator fun component2(): ItemStack? = item
    operator fun component3(): BlockData? = blockData
    operator fun component4(): Entity? = entity

    /**
     * Holds the block and block face of an event.
     */
    data class BlockData(
        val block: Block,
        val blockFace: BlockFace
    )
}
