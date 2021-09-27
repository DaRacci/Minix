package me.racci.raccicore.utils.items.builders


import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkEffectMeta
import org.bukkit.inventory.meta.FireworkMeta

/**
 * Item builder for [Material.FIREWORK_ROCKET] and [Material.FIREWORK_ROCKET] only
 *
 * @author GabyTM [https://github.com/iGabyTM](https://github.com/iGabyTM)
 * @since 3.0.1
 */
class FireworkBuilder internal constructor(itemStack: ItemStack) : BaseItemBuilder<FireworkBuilder>(itemStack) {
    /**
     * Add several firework effects to this firework.
     *
     * @param effects effects to add
     * @return [FireworkBuilder]
     * @throws IllegalArgumentException If effects is null
     * @throws IllegalArgumentException If any effect is null (maybe thrown after changes have occurred)
     * @since 3.0.1
     */
    fun effect(vararg effects: FireworkEffect?): FireworkBuilder {
        return effect(listOf(*effects))
    }

    /**
     * Add several firework effects to this firework.
     *
     * @param effects effects to add
     * @return [FireworkBuilder]
     * @throws IllegalArgumentException If effects is null
     * @throws IllegalArgumentException If any effect is null (maybe thrown after changes have occurred)
     * @since 3.0.1
     */
    private fun effect(effects: List<FireworkEffect?>): FireworkBuilder {
        if (effects.isEmpty()) {
            return this
        }
        if (getItemStack().type == STAR) {
            val effectMeta = getMeta() as FireworkEffectMeta
            effectMeta.effect = effects[0]
            setMeta(effectMeta)
            return this
        }
        val fireworkMeta = getMeta() as FireworkMeta
        fireworkMeta.addEffects(effects)
        setMeta(fireworkMeta)
        return this
    }

    /**
     * Sets the approximate power of the firework. Each level of power is half
     * a second of flight time.
     *
     * @param power the power of the firework, from 0-128
     * @return [FireworkBuilder]
     * @throws IllegalArgumentException if height&lt;0 or height&gt;128
     * @since 3.0.1
     */
    fun power(power: Int): FireworkBuilder {
        if (getItemStack().type == ROCKET) {
            val fireworkMeta = getMeta() as FireworkMeta
            fireworkMeta.power = power
            setMeta(fireworkMeta)
        }
        return this
    }

    companion object {
        private val STAR = Material.FIREWORK_STAR
        private val ROCKET = Material.FIREWORK_ROCKET
    }

    init {
        if (itemStack.type != STAR && itemStack.type != ROCKET) {
            throw UnsupportedOperationException("FireworkBuilder requires the material to be a FIREWORK_STAR/FIREWORK_ROCKET!")
        }
    }
}