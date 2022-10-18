package dev.racci.minix.api.paper.builders

import org.bukkit.FireworkEffect
import org.bukkit.inventory.meta.FireworkMeta

interface FireworkBuilder : BaseItemBuilder<FireworkBuilder, FireworkMeta> {

    /**
     * Clear and set the effects of the firework.
     */
    var effects: List<FireworkEffect>

    /**
     * Set the power of the firework.
     */
    var power: Int

    /**
     * Add the effects to the firework.
     */
    fun addEffects(vararg effects: FireworkEffect): FireworkBuilder
}
