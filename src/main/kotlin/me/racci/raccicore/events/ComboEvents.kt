package me.racci.raccicore.events

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

data class BlockData(
    val block: Block,
    val blockFace: BlockFace
)

class PlayerShiftLeftClickEvent(
    player: Player,
    item: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null,
): AbstractComboEvent(player, item, blockData, entity)

class PlayerLeftClickEvent(
    player: Player,
    item: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null,
): AbstractComboEvent(player, item, blockData, entity)

class PlayerShiftRightClickEvent(
    player: Player,
    item: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null,
): AbstractComboEvent(player, item, blockData, entity)

class PlayerRightClickEvent(
    player: Player,
    item: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null,
): AbstractComboEvent(player, item, blockData, entity)

class PlayerShiftOffhandEvent(
    player: Player,
    item: ItemStack?,
    val offHandItem: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null,
): AbstractComboEvent(player, item, blockData, entity) {

    /**
     * If true this means that either one hand or both hands have
     * an item and are not null.
     */
    override val hasItem
        get() = item != null || offHandItem != null

}

class PlayerOffhandEvent(
    player: Player,
    item: ItemStack?,
    val offHandItem: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null,
): AbstractComboEvent(player, item, blockData, entity) {

    /**
     * If true this means that either one hand or both hands have
     * an item and are not null.
     */
    override val hasItem
        get() = item != null || offHandItem != null

}

class PlayerShiftDoubleOffhandEvent(
    player: Player,
    item: ItemStack?,
    val offHandItem: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null,
): AbstractComboEvent(player, item, blockData, entity) {

    /**
     * If true this means that either one hand or both hands have
     * an item and are not null.
     */
    override val hasItem
        get() = item != null || offHandItem != null

}

class PlayerDoubleOffhandEvent(
    player: Player,
    item: ItemStack?,
    val offHandItem: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null,
): AbstractComboEvent(player, item, blockData, entity) {

    /**
     * If true this means that either one hand or both hands have
     * an item and are not null.
     */
    override val hasItem
        get() = item != null || offHandItem != null

}
