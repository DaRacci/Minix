package dev.racci.minix.core.builders

import dev.racci.minix.api.paper.builders.FireworkBuilder
import org.bukkit.FireworkEffect
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta

public class FireworkBuilderImpl internal constructor(
    itemStack: ItemStack
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
