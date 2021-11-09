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

class PlayerLeftClickBlockEvent(
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

class PlayerRightClickBlockEvent(
    player: Player,
    override val item: ItemStack?,
    override val block: Block,
    override val blockFace: BlockFace,
): KPlayerEvent(player, true), IComboBlockEvent {

    override val hasItem: Boolean = item != null

}