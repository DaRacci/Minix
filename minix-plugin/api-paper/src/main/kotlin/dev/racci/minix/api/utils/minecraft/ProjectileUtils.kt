package dev.racci.minix.api.utils.minecraft

import org.bukkit.entity.Arrow
import org.bukkit.inventory.ItemStack

public object ProjectileUtils {

    /**
     * Get the bow of an arrow.
     *
     * @param arrow the arrow
     * @return The bow or null
     */
    public fun getBow(arrow: Arrow): ItemStack? {
        val values = arrow.getMetadata("shot-from")
        return if (values.isEmpty() || values[0].value() !is ItemStack) {
            null
        } else values[0].value() as ItemStack
    }
}
