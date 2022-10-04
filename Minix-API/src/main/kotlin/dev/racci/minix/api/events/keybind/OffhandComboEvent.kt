package dev.racci.minix.api.events.keybind

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Base class for combo events that include the offhand.
 *
 * @param player The player who triggered the event.
 * @param item The item present in the players main hand during the event.
 * @param offHandItem The item present in the players offhand during the event.
 * @param blockData The block data present at the location of the event.
 * @param entity The entity present at the location of the event.
 */
sealed class OffhandComboEvent(
    player: Player,
    item: ItemStack?,
    val offHandItem: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null
) : ComboEvent(player, item, blockData, entity) {

    /**
     * If true, this means that either one hand or both hands have
     * an item and are not null.
     */
    override val hasItem: Boolean get() = item != null || offHandItem != null

    operator fun component5(): ItemStack? = offHandItem
}
