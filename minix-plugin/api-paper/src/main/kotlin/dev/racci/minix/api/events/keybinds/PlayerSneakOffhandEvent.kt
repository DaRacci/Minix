package dev.racci.minix.api.events.keybinds

import dev.racci.minix.api.events.CompanionEventHandler
import dev.racci.minix.api.data.MinixPlayer
import org.bukkit.entity.Entity
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

/**
 * Called when the player uses the combo of Shift + Left click.
 *
 * @param player The player who triggered the event.
 * @param item The item present in the players main hand during the event.
 * @param offHandItem The item present in the players offhand during the event.
 * @param blockData The block data present at the location of the event.
 * @param entity The entity present at the location of the event.
 */
public class PlayerSneakOffhandEvent(
    player: MinixPlayer,
    item: ItemStack?,
    offHandItem: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null
) : OffhandComboEvent(player, item, offHandItem, blockData, entity) {
    public companion object : CompanionEventHandler() {
        @JvmStatic
        override fun getHandlerList(): HandlerList = super.getHandlerList()
    }
}
