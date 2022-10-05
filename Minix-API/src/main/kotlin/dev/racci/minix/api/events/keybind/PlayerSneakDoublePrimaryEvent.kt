package dev.racci.minix.api.events.keybind

import dev.racci.minix.api.events.CompanionEventHandler
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Called when the player uses the combo of Shift + Right click.
 * ## Note: Will not be called if the player clicks the air without an item in either hand due to the client not sending these packets.
 *
 * @param player The player who triggered the event.
 * @param item The item present in the players main hand during the event.
 * @param blockData The block data present at the location of the event.
 * @param entity The entity present at the location of the event.
 */
class PlayerSneakDoublePrimaryEvent(
    player: Player,
    item: ItemStack?,
    blockData: BlockData? = null,
    entity: Entity? = null
) : ComboEvent(player, item, blockData, entity) {
    companion object : CompanionEventHandler() {
        @JvmStatic
        override fun getHandlerList() = super.getHandlerList()
    }
}
