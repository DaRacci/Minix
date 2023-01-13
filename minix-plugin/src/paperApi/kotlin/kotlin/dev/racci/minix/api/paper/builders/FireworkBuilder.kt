package dev.racci.minix.api.paper.builders

import org.bukkit.FireworkEffect
import org.bukkit.inventory.meta.FireworkMeta

public interface FireworkBuilder : BaseItemBuilder<FireworkBuilder, FireworkMeta> {

    /**
     * Clear and set the effects of the firework.
     */
    public var effects: List<FireworkEffect>

    /**
     * Set the power of the firework.
     */
    public var power: Int

    /**
     * Add the effects to the firework.
     */
    public fun addEffects(vararg effects: FireworkEffect): FireworkBuilder
}
