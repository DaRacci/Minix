package dev.racci.minix.api.events.keybind

import dev.racci.minix.api.events.CompanionEventHandler
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Called when the player uses the offhand key.
 *
 * @param player The player who triggered the event.
 * @param item The item present in the players main hand during the event.
 * @param offHandItem The item present in the players offhand during the event.
 * @param blockData The block data present at the location of the event.
 * @param entity The entity present at the location of the event.
 */
class PlayerOffhandEvent(
    player: Player,
    item: ItemStack?,
    offHandItem: ItemStack?,
    blockData: BlockData?,
    entity: Entity?
) : OffhandComboEvent(player, item, offHandItem, blockData, entity) {
    companion object : CompanionEventHandler() {
        @JvmStatic
        override fun getHandlerList() = super.getHandlerList()
    }
}
