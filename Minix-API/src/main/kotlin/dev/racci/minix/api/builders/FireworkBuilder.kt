@file:Suppress("UNUSED")
package dev.racci.minix.api.builders

import dev.racci.minix.api.annotations.MinixDsl
import org.bukkit.FireworkEffect
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta

/**
 * DSL Item builder for fireworks.
 */
@MinixDsl
class FireworkBuilder internal constructor(
    itemStack: ItemStack
): BaseItemBuilder<FireworkBuilder, FireworkMeta>(itemStack) {

    /**
     * Clear and set the effects of the firework.
     */
    var effects: List<FireworkEffect>
        get() = meta.effects
        set(effects) { meta.clearEffects() ; meta.addEffects(effects) }

    /**
     * Set the power of the firework.
     */
    var power: Int
        get() = meta.power
        set(power) { meta.power = power }

    /**
     * Add the effects to the firework.
     */
    fun addEffects(vararg effects: FireworkEffect): FireworkBuilder {
        meta.addEffects(*effects)
        return this
    }
}
