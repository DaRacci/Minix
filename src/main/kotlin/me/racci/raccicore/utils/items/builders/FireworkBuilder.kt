package me.racci.raccicore.utils.items.builders


import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkEffectMeta
import org.bukkit.inventory.meta.FireworkMeta
import org.jetbrains.annotations.ApiStatus

@Deprecated("Moved and improved.", ReplaceWith("me.racci.raccicore.builders.FireworkBuilder"))
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
class FireworkBuilder internal constructor(itemStack: ItemStack) : BaseItemBuilder<FireworkBuilder>(itemStack) {

    private val feMeta get() = meta as FireworkEffectMeta
    private val fMeta get() = meta as FireworkMeta

    /**
     * Add several firework effects to this firework.
     *
     * @param effects effects to add
     * @return [FireworkBuilder]
     * @throws IllegalArgumentException If effects is null
     * @throws IllegalArgumentException If any effect is null (maybe thrown after changes have occurred)
     * @since 3.0.1
     */
    fun effect(vararg effects: FireworkEffect): FireworkBuilder {
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
    private fun effect(effects: List<FireworkEffect>): FireworkBuilder {
        if (effects.isEmpty()) {
            return this
        }
        if (itemStack.type == STAR) {
            feMeta.effect = effects[0]
            return this
        }
        fMeta.addEffects(effects)
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
        if (itemStack.type == ROCKET) {
            fMeta.power = power
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