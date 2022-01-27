@file:Suppress("UNUSED")

package dev.racci.minix.core.builders

import dev.racci.minix.api.builders.FireworkBuilder
import org.bukkit.FireworkEffect
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta
import org.jetbrains.annotations.ApiStatus

/**
 * DSL Item builder for fireworks.
 */
@ApiStatus.AvailableSince("0.1.5")
class FireworkBuilderImpl internal constructor(
    itemStack: ItemStack,
) : BaseItemBuilderImpl<FireworkBuilder, FireworkMeta>(itemStack), FireworkBuilder {

    override var effects: List<FireworkEffect>
        get() = meta.effects
        set(effects) {
            meta.clearEffects(); meta.addEffects(effects)
        }

    override var power: Int
        get() = meta.power
        set(power) {
            meta.power = power
        }

    override fun addEffects(vararg effects: FireworkEffect): FireworkBuilderImpl {
        meta.addEffects(*effects)
        return this
    }
}
