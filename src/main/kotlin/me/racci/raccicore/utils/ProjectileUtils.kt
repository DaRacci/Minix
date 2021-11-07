package me.racci.raccicore.utils

import org.bukkit.entity.Arrow
import org.bukkit.inventory.ItemStack

object ProjectileUtils {

    /**
     * Get the bow of an arrow
     *
     * @param arrow the arrow
     * @return The bow or null
     */
    fun getBow(arrow: Arrow): ItemStack? {
        val values = arrow.getMetadata("shot-from")
        if(values.isEmpty()) return null
        if(values[0].value() !is ItemStack) return null
        return values[0].value() as ItemStack
    }


}