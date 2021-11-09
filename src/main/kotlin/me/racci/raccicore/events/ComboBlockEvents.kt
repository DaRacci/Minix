package me.racci.raccicore.events

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PlayerShiftLeftClickBlockEvent(
    player: Player,
    override val item: ItemStack?,
    override val block: Block,
    override val blockFace: BlockFace,
): KPlayerEvent(player, true), IComboBlockEvent {

    override val hasItem: Boolean = item != null

}

class PlayerShiftRightClickBlockEvent(
    player: Player,
    override val item: ItemStack?,
    override val block: Block,
    override val blockFace: BlockFace,
): KPlayerEvent(player, true), IComboBlockEvent {

    override val hasItem: Boolean = item != null

}

class PlayerShiftOffhandClickBlockEvent(
    player: Player,
    override val item: ItemStack?,
    override val block: Block,
    override val blockFace: BlockFace,
): KPlayerEvent(player, true), IComboBlockEvent {

    override val hasItem: Boolean = item != null

}

class PlayerShiftDoubleOffhandClickBlockEvent(
    player: Player,
    override val item: ItemStack?,
    override val block: Block,
    override val blockFace: BlockFace,
): KPlayerEvent(player, true), IComboBlockEvent {

    override val hasItem: Boolean = item != null

}