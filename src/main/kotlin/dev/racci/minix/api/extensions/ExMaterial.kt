package dev.racci.minix.api.extensions

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Converts the material to a new [ItemStack] with
 * default amount of 1 and applies the given block to it after.
 * @return ItemStack
 * @since 1.0.0
 */
inline fun Material.toItemStack(
    amount: Int = 1,
    block: ItemStack.() -> Unit = {},
) = ItemStack(this, amount).apply(block)
