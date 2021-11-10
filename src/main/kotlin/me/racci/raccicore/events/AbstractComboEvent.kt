package me.racci.raccicore.events

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class AbstractComboEvent(
    player: Player,
    val item: ItemStack?,
    val blockData: BlockData? = null,
    val entity: Entity? = null,
): KPlayerEvent(player, true) {

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

}