package dev.racci.minix.api.events.keybinds

import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.events.player.MinixPlayerEvent
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack

/**
 * Base class for all combo events.
 *
 * @param player The player who triggered the event.
 * @param item The item present in the players main hand during the event.
 * @param blockData The block data present at the location of the event.
 * @param entity The entity present at the location of the event.
 */
public sealed class ComboEvent(
    player: MinixPlayer,
    public val item: ItemStack?,
    public val blockData: BlockData? = null,
    public val entity: Entity? = null
) : MinixPlayerEvent(player, true) {

    /**
     * If true, this means the used item in the event is not a players fists.
     */
    public open val hasItem: Boolean get() = item != null

    /**
     * Returns if this event had an entity Involved.
     */
    public val isEntityEvent: Boolean get() = entity != null

    /**
     * Returns if this event had a block involved.
     */
    public val isBlockEvent: Boolean get() = blockData != null

    public operator fun component2(): ItemStack? = item
    public operator fun component3(): BlockData? = blockData
    public operator fun component4(): Entity? = entity

    /**
     * Holds the block and block face of an event.
     */
    public data class BlockData(
        val block: Block,
        val blockFace: BlockFace
    )
}
