package me.racci.raccicore.events

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PlayerShiftLeftClickEntityEvent(
    player: Player,
    override val item: ItemStack?,
    override val entity: LivingEntity,
): KPlayerEvent(player, true), IComboEntityEvent {

    override val hasItem: Boolean = item != null

}

class PlayerShiftRightClickEntityEvent(
    player: Player,
    override val item: ItemStack?,
    override val entity: LivingEntity,
): KPlayerEvent(player, true), IComboEntityEvent {

    override val hasItem: Boolean = item != null

}

class PlayerShiftOffhandClickEntityEvent(
    player: Player,
    override val item: ItemStack?,
    override val entity: LivingEntity,
): KPlayerEvent(player, true), IComboEntityEvent {

    override val hasItem: Boolean = item != null

}

class PlayerShiftDoubleOffhandClickEntityEvent(
    player: Player,
    override val item: ItemStack?,
    override val entity: LivingEntity,
): KPlayerEvent(player, true), IComboEntityEvent {

    override val hasItem: Boolean = item != null

}