package me.racci.raccicore.extensions

import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack

val ItemStack.pdc get() = persistentDataContainer
val Entity.pdc get() = persistentDataContainer