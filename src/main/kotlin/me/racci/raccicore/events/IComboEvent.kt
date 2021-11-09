package me.racci.raccicore.events

import org.bukkit.inventory.ItemStack

interface IComboEvent {

    val item: ItemStack?

    val hasItem: Boolean

}