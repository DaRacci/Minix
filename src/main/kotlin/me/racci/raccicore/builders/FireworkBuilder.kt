package me.racci.raccicore.builders

import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkEffectMeta
import org.bukkit.inventory.meta.FireworkMeta

class FireworkBuilder internal constructor(itemStack: ItemStack) : BaseItemBuilder<FireworkBuilder>(itemStack) {

    fun effect(vararg effects: FireworkEffect): FireworkBuilder {
        if (effects.isEmpty()) {
            return this
        }
        if (itemStack.type == Material.FIREWORK_STAR) {
            (meta as FireworkEffectMeta).effect = effects[0]
            return this
        }
        (meta as FireworkMeta).addEffects(*effects)
        return this
    }

    fun power(power: Int): FireworkBuilder {
        if (itemStack.type == Material.FIREWORK_ROCKET) {
            (meta as FireworkMeta).power = power
        }
        return this
    }

    init {
        if (itemStack.type != Material.FIREWORK_STAR && itemStack.type != Material.FIREWORK_ROCKET) {
            throw UnsupportedOperationException("FireworkBuilder requires the material to be a FIREWORK_STAR/FIREWORK_ROCKET!")
        }
    }
}