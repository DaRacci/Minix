package me.racci.raccicore.events

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PlayerShiftLeftClickEntityEvent(
    player: Player,
    override val item: ItemStack?,
    override val entity: Entity,
): KPlayerEvent(player, true), IComboEntityEvent {

    override val hasItem: Boolean = item != null

}

class PlayerLeftClickEntityEvent(
    player: Player,
    override val item: ItemStack?,
    override val entity: Entity,
): KPlayerEvent(player, true), IComboEntityEvent {

    override val hasItem: Boolean = item != null

}

class PlayerShiftRightClickEntityEvent(
    player: Player,
    override val item: ItemStack?,
    override val entity: Entity,
): KPlayerEvent(player, true), IComboEntityEvent {

    override val hasItem: Boolean = item != null

}

class PlayerRightClickEntityEvent(
    player: Player,
    override val item: ItemStack?,
    override val entity: Entity,
): KPlayerEvent(player, true), IComboEntityEvent {

    override val hasItem: Boolean = item != null

}