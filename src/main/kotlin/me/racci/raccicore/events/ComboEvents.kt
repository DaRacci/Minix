package me.racci.raccicore.events

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PlayerShiftLeftClickEvent(
    player: Player,
    override val item: ItemStack?,
): KPlayerEvent(player, true), IComboEvent {

    override val hasItem: Boolean = item != null

}

class PlayerShiftRightClickEvent(
    player: Player,
    override val item: ItemStack?,
): KPlayerEvent(player, true), IComboEvent {

    override val hasItem: Boolean = item != null

}

class PlayerShiftOffhandEvent(
    player: Player,
    override val item: ItemStack?,
): KPlayerEvent(player, true), IComboEvent {

    override val hasItem: Boolean = item != null

}

class PlayerShiftDoubleOffhandEvent(
    player: Player,
    override val item: ItemStack?,
): KPlayerEvent(player, true), IComboEvent {

    override val hasItem: Boolean = item != null

}

class PlayerDoubleOffhandEvent(
    player: Player,
    override val item: ItemStack?
): KPlayerEvent(player, true), IComboEvent {

    override val hasItem: Boolean = item != null

}