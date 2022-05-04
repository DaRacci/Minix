@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package dev.racci.minix.api.events

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack
import org.jetbrains.annotations.ApiStatus

/**
 * Holds the block and block face of an event.
 */
data class BlockData(
    val block: Block,
    val blockFace: BlockFace
)

/**
 * Base class for all combo events.
 */
@ApiStatus.Internal
sealed class AbstractComboEvent(
    player: Player,
    /**
     * The item in use during this event.
     */
    val item: ItemStack?,
    /**
     * The block being effected during this event.
     */
    val blockData: BlockData? = null,
    /**
     * The entity being interacted with during this event.
     */
    val entity: Entity? = null
) : KPlayerEvent(player, true) {

    /**
     * If true this means the used item in the event is not a players fists.
     */
    open val hasItem
        get() = item != null

    /**
     * Returns if this event had an entity Involved.
     */
    val isEntityEvent
        get() = entity != null

    /**
     * Returns if this event had a block involved.
     */
    val isBlockEvent
        get() = blockData != null

    operator fun component2(): ItemStack? = item
    operator fun component3(): BlockData? = blockData
    operator fun component4(): Entity? = entity
}
/**
 * Called when the player left clicks.
 */
class PlayerLeftClickEvent(
    player: Player,
    item: ItemStack?,
    blockData: BlockData?,
    entity: Entity?
) : AbstractComboEvent(player, item, blockData, entity) {
    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerLeftClickEvent::class]
    }
}
/**
 * Called when the player right clicks.
 * ## Note: Will not be called if the player clicks the air without an item in either hand due to the client not sending these packets.
 */
class PlayerRightClickEvent(
    player: Player,
    item: ItemStack?,
    blockData: BlockData?,
    entity: Entity?
) : AbstractComboEvent(player, item, blockData, entity) {
    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerRightClickEvent::class]
    }
}
/**
 * Called when the player uses the offhand key.
 */
class PlayerOffhandEvent(
    player: Player,
    item: ItemStack?,
    /**
     * The item present in the players offhand during the event.
     */
    val offHandItem: ItemStack?,
    blockData: BlockData?,
    entity: Entity?
) : AbstractComboEvent(player, item, blockData, entity) {
    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerOffhandEvent::class]
    }
}

/**
 * Called when the player double left clicks.
 */
class PlayerDoubleLeftClickEvent(
    player: Player,
    item: ItemStack?,
    blockData: BlockData?,
    entity: Entity?
) : AbstractComboEvent(player, item, blockData, entity) {
    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerDoubleLeftClickEvent::class]
    }
}

/**
 * Called when the player double right clicks.
 * ## Note: Will not be called if the player clicks the air without an item in either hand due to the client not sending these packets.
 */
class PlayerDoubleRightClickEvent(
    player: Player,
    item: ItemStack?,
    blockData: BlockData?,
    entity: Entity?
) : AbstractComboEvent(player, item, blockData, entity) {
    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerDoubleRightClickEvent::class]
    }
}

/**
 * Called when the player uses offhand twice within 0.5 seconds.
 */
class PlayerDoubleOffhandEvent(
    player: Player,
    item: ItemStack?,
    /**
     * The item present in the players offhand during the event.
     */
    val offHandItem: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null
) : AbstractComboEvent(player, item, blockData, entity) {

    /**
     * If true this means that either one hand or both hands have
     * an item and are not null.
     */
    override val hasItem
        get() = item != null || offHandItem != null

    operator fun component5(): ItemStack? = offHandItem

    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerDoubleOffhandEvent::class]
    }
}
/**
 * Called when the player double left clicks while holding shift.
 */
class PlayerShiftDoubleLeftClickEvent(
    player: Player,
    item: ItemStack?,
    blockData: BlockData?,
    entity: Entity?
) : AbstractComboEvent(player, item, blockData, entity) {
    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerShiftDoubleLeftClickEvent::class]
    }
}

/**
 * Called when the player double right clicks while holding shift.
 * ## Note: Will not be called if the player clicks the air without an item in either hand due to the client not sending these packets.
 */
class PlayerShiftDoubleRightClickEvent(
    player: Player,
    item: ItemStack?,
    blockData: BlockData?,
    entity: Entity?
) : AbstractComboEvent(player, item, blockData, entity) {
    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerShiftDoubleRightClickEvent::class]
    }
}

/**
 * Called when the player uses the combo of Shift + Left click.
 */
class PlayerShiftLeftClickEvent(
    player: Player,
    item: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null
) : AbstractComboEvent(player, item, blockData, entity) {
    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerShiftLeftClickEvent::class]
    }
}

/**
 * Called when the player uses the combo of Shift + Right click.
 * ## Note: Will not be called if the player clicks the air without an item in either hand due to the client not sending these packets.
 */
class PlayerShiftRightClickEvent(
    player: Player,
    item: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null
) : AbstractComboEvent(player, item, blockData, entity) {
    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerShiftRightClickEvent::class]
    }
}

/**
 * Called when the player uses the combo of Shift + Offhand.
 */
class PlayerShiftOffhandEvent(
    player: Player,
    item: ItemStack?,
    /**
     * The item present in the players offhand during the event.
     */
    val offHandItem: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null
) : AbstractComboEvent(player, item, blockData, entity) {

    /**
     * If true this means that either one hand or both hands have
     * an item and are not null.
     */
    override val hasItem
        get() = item != null || offHandItem != null

    operator fun component5(): ItemStack? = offHandItem

    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerShiftOffhandEvent::class]
    }
}

/**
 * Called when the player uses offhand twice within 0.5 seconds
 * while holding shift.
 */
class PlayerShiftDoubleOffhandEvent(
    player: Player,
    item: ItemStack?,
    /**
     * The item present in the players offhand during the event.
     */
    val offHandItem: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null
) : AbstractComboEvent(player, item, blockData, entity) {

    /**
     * If true this means that either one hand or both hands have
     * an item and are not null.
     */
    override val hasItem
        get() = item != null || offHandItem != null

    operator fun component5(): ItemStack? = offHandItem

    companion object {
        @JvmStatic
        fun getHandlerList(): HandlerList = KEvent.handlerMap[PlayerShiftDoubleOffhandEvent::class]
    }
}
