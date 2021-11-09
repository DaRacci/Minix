package me.racci.raccicore.events

/*import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Player shift left click event
 *
 * @property player
 * @property usedItem
 * @property clickedLocation
 * @property clickedBlock
 * @property clickedBlockFace
 * @property clickedEntity
 * @constructor Create empty Player shift left click event
 */
@Deprecated("Moving to more specific systems" ReplaceWith(""))
class PlayerShiftLeftClickEvent(
    val player: Player,
    val usedItem: ItemStack?,
    val clickedLocation: Location?,
    val clickedBlock: Block?,
    val clickedBlockFace: BlockFace?,
    val clickedEntity: Entity?,
) : KotlinEvent(true) {

    /**
     * Has item
     */
    val hasItem: Boolean get() = usedItem == null

    /**
     * Has block
     */
    val hasBlock: Boolean get() = clickedBlock == null

}

/**
 * Player shift right click event
 *
 * @property player
 * @property usedItem
 * @property clickedLocation
 * @property clickedBlock
 * @property clickedBlockFace
 * @property clickedEntity
 * @constructor Create empty Player shift right click event
 */
class PlayerShiftRightClickEvent(
    val player: Player,
    val usedItem: ItemStack?,
    val clickedLocation: Location?,
    val clickedBlock: Block?,
    val clickedBlockFace: BlockFace?,
    val clickedEntity: Entity?,
) : KotlinEvent(true) {

    /**
     * Has item
     */
    val hasItem: Boolean get() = usedItem == null

    /**
     * Has block
     */
    val hasBlock: Boolean get() = clickedBlock == null

}

/**
 * Player shift offhand event
 *
 * @property player
 * @property mainItem
 * @property offhandItem
 * @constructor Create empty Player shift offhand event
 */
class PlayerShiftOffhandEvent(
    val player: Player,
    val mainItem: ItemStack?,
    val offhandItem: ItemStack?
) : KotlinEvent(true) {

    /**
     * Has main
     */
    val hasMain get() = mainItem != null

    /**
     * Has offhand
     */
    val hasOffhand get() = offhandItem != null

}

/**
 * Player shift double offhand event
 *
 * @property player
 * @property mainItem
 * @property offhandItem
 * @constructor Create empty Player shift double offhand event
 */
class PlayerShiftDoubleOffhandEvent(
    val player: Player,
    val mainItem: ItemStack?,
    val offhandItem: ItemStack?
) : KotlinEvent(true) {

    /**
     * Has main
     */
    val hasMain get() = mainItem != null

    /**
     * Has offhand
     */
    val hasOffhand get() = offhandItem != null

}

/**
 * Player left click event
 *
 * @property player
 * @property usedItem
 * @property clickedLocation
 * @property clickedBlock
 * @property clickedBlockFace
 * @property clickedEntity
 * @constructor Create empty Player left click event
 */
class PlayerLeftClickEvent(
    val player: Player,
    val usedItem: ItemStack?,
    val clickedLocation: Location?,
    val clickedBlock: Block?,
    val clickedBlockFace: BlockFace?,
    val clickedEntity: Entity?,
) : KotlinEvent(true) {

    /**
     * Has item
     */
    val hasItem: Boolean get() = usedItem == null

    /**
     * Has block
     */
    val hasBlock: Boolean get() = clickedBlock == null

}

/**
 * Player right click event
 *
 * @property player
 * @property usedItem
 * @property clickedLocation
 * @property clickedBlock
 * @property clickedBlockFace
 * @property clickedEntity
 * @constructor Create empty Player right click event
 */
class PlayerRightClickEvent(
    val player: Player,
    val usedItem: ItemStack?,
    val clickedLocation: Location?,
    val clickedBlock: Block?,
    val clickedBlockFace: BlockFace?,
    val clickedEntity: Entity?,
) : KotlinEvent(true) {

    /**
     * Has item
     */
    val hasItem: Boolean get() = usedItem == null

    /**
     * Has block
     */
    val hasBlock: Boolean get() = clickedBlock == null

}

/**
 * Player offhand event
 *
 * @property player
 * @property mainItem
 * @property offhandItem
 * @constructor Create empty Player offhand event
 */
class PlayerOffhandEvent(
    val player: Player,
    val mainItem: ItemStack?,
    val offhandItem: ItemStack?
) : KotlinEvent(true) {

    /**
     * Has main
     */
    val hasMain get() = mainItem != null

    /**
     * Has offhand
     */
    val hasOffhand get() = offhandItem != null

}

/**
 * Player double offhand event
 *
 * @property player
 * @property mainItem
 * @property offhandItem
 * @constructor Create empty Player double offhand event
 */
class PlayerDoubleOffhandEvent(
    val player: Player,
    val mainItem: ItemStack?,
    val offhandItem: ItemStack?
) : KotlinEvent(true) {

    /**
     * Has main
     */
    val hasMain get() = mainItem != null

    /**
     * Has offhand
     */
    val hasOffhand get() = offhandItem != null

}*/