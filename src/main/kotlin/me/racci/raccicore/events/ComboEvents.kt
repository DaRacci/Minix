package me.racci.raccicore.events

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PlayerShiftLeftClickEvent(
    player: Player,
    override val item: ItemStack?,
): KPlayerEvent(player, true), IComboEvent {

    /**
     * If true this means the used item in the event is not a players fists.
     */
    override val hasItem: Boolean = item != null

}

class PlayerLeftClickEvent(
    player: Player,
    override val item: ItemStack?,
): KPlayerEvent(player, true), IComboEvent {

    /**
     * If true this means the used item in the event is not a players fists.
     */
    override val hasItem: Boolean = item != null

}

class PlayerShiftRightClickEvent(
    player: Player,
    override val item: ItemStack?,
): KPlayerEvent(player, true), IComboEvent {

    /**
     * If true this means the used item in the event is not a players fists.
     */
    override val hasItem: Boolean = item != null

}

class PlayerRightClickEvent(
    player: Player,
    override val item: ItemStack?,
): KPlayerEvent(player, true), IComboEvent {

    /**
     * If true this means the used item in the event is not a players fists.
     */
    override val hasItem: Boolean = item != null

}

class PlayerShiftOffhandEvent(
    player: Player,
    override val item: ItemStack?,
    val offHandItem: ItemStack?,
): KPlayerEvent(player, true), IComboEvent {

    /**
     * If true this means that either one hand or both hands have
     * an item and are not null.
     */
    override val hasItem: Boolean = item != null

}

class PlayerOffhandEvent(
    player: Player,
    override val item: ItemStack?,
    val offHandItem: ItemStack?,
): KPlayerEvent(player, true), IComboEvent {

    /**
     * If true this means that either one hand or both hands have
     * an item and are not null.
     */
    override val hasItem: Boolean = item != null

}

class PlayerShiftDoubleOffhandEvent(
    player: Player,
    override val item: ItemStack?,
    val offHandItem: ItemStack?,
): KPlayerEvent(player, true), IComboEvent {

    /**
     * If true this means that either one hand or both hands have
     * an item and are not null.
     */
    override val hasItem: Boolean = item != null || offHandItem != null

}

class PlayerDoubleOffhandEvent(
    player: Player,
    override val item: ItemStack?,
    val offHandItem: ItemStack?,
): KPlayerEvent(player, true), IComboEvent {

    /**
     * If true this means that either one hand or both hands have
     * an item and are not null.
     */
    override val hasItem: Boolean = item != null

}

