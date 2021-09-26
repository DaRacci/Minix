package me.racci.raccilib.events

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PlayerShiftLeftClickEvent(
    val player: Player,
    val usedItem: ItemStack?,
    val clickedLocation: Location?,
    val clickedBlock: Block?,
    val clickedBlockFace: BlockFace?,
    val clickedEntity: Entity?,
) : KotlinEvent(true) {

    val hasItem: Boolean get() = usedItem == null

    val hasBlock: Boolean get() = clickedBlock == null

}

class PlayerShiftRightClickEvent(
    val player: Player,
    val usedItem: ItemStack?,
    val clickedLocation: Location?,
    val clickedBlock: Block?,
    val clickedBlockFace: BlockFace?,
    val clickedEntity: Entity?,
) : KotlinEvent(true) {

    val hasItem: Boolean get() = usedItem == null

    val hasBlock: Boolean get() = clickedBlock == null

}

class PlayerShiftOffhandEvent(
    val player: Player,
    val mainItem: ItemStack?,
    val offhandItem: ItemStack?
) : KotlinEvent(true) {

    val hasMain get() = mainItem != null
    val hasOffhand get() = offhandItem != null

}

class PlayerShiftDoubleOffhandEvent(
    val player: Player,
    val mainItem: ItemStack?,
    val offhandItem: ItemStack?
) : KotlinEvent(true) {

    val hasMain get() = mainItem != null
    val hasOffhand get() = offhandItem != null

}

class PlayerLeftClickEvent(
    val player: Player,
    val usedItem: ItemStack?,
    val clickedLocation: Location?,
    val clickedBlock: Block?,
    val clickedBlockFace: BlockFace?,
    val clickedEntity: Entity?,
) : KotlinEvent(true) {

    val hasItem: Boolean get() = usedItem == null

    val hasBlock: Boolean get() = clickedBlock == null

}

class PlayerRightClickEvent(
    val player: Player,
    val usedItem: ItemStack?,
    val clickedLocation: Location?,
    val clickedBlock: Block?,
    val clickedBlockFace: BlockFace?,
    val clickedEntity: Entity?,
) : KotlinEvent(true) {

    val hasItem: Boolean get() = usedItem == null

    val hasBlock: Boolean get() = clickedBlock == null

}

class PlayerOffhandEvent(
    val player: Player,
    val mainItem: ItemStack?,
    val offhandItem: ItemStack?
) : KotlinEvent(true) {

    val hasMain get() = mainItem != null
    val hasOffhand get() = offhandItem != null

}

class PlayerDoubleOffhandEvent(
    val player: Player,
    val mainItem: ItemStack?,
    val offhandItem: ItemStack?
) : KotlinEvent(true) {

    val hasMain get() = mainItem != null
    val hasOffhand get() = offhandItem != null

}