@file:Suppress("UNCHECKED_CAST", "UNUSED")

package dev.racci.minix.core.builders

import com.destroystokyo.paper.Namespaced
import com.google.common.collect.Multimap
import dev.racci.minix.api.builders.BaseItemBuilder
import dev.racci.minix.api.extensions.PDC
import dev.racci.minix.api.extensions.pdc
import dev.racci.minix.api.extensions.toItemStack
import dev.racci.minix.api.utils.IncorrectItemTypeException
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

sealed class BaseItemBuilderImpl<B : BaseItemBuilder<B, M>, M : ItemMeta> constructor(
    override var itemStack: ItemStack = Material.AIR.toItemStack(),
    override var meta: M = itemStack.itemMeta as? M
        ?: throw IncorrectItemTypeException("The meta type ${Class<M>::getName} and builder type" + " ${Class<B>::getName} is not valid for item type ${itemStack.type.name}"),
) : BaseItemBuilder<B, M> {

    override var amount: Int
        get() = itemStack.amount
        set(amount) {
            itemStack.amount = amount
        }

    override var name: Component?
        get() = meta.displayName()
        set(name) {
            meta.displayName(name)
        }

    override var lore: List<Component>
        get() = meta.lore().orEmpty()
        set(lore) {
            meta.lore(lore)
        }

    override var model: Int?
        get() = if (meta.hasCustomModelData()) meta.customModelData else null
        set(model) = meta.setCustomModelData(model)

    override var itemFlags: Set<ItemFlag>
        get() = meta.itemFlags
        set(flags) {
            addFlag(*flags.toTypedArray())
        }

    override var isUnbreakable: Boolean
        get() = meta.isUnbreakable
        set(boolean) {
            meta.isUnbreakable = boolean
        }

    override var glowing: Boolean
        get() = meta.hasEnchants() && meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)
        set(boolean) {
            if (boolean) {
                if (!meta.hasEnchants()) {
                    meta.addEnchant(Enchantment.DURABILITY, 1, false)
                }
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            } else {
                removeEnchant(*meta.enchants.keys.toTypedArray())
            }
        }

    override var attributeModifiers: Multimap<Attribute, AttributeModifier>?
        get() = meta.attributeModifiers
        set(multiMap) {
            meta.attributeModifiers = multiMap
        }

    override var destroyableKeys: Set<Namespaced>
        get() = meta.destroyableKeys
        set(keys) {
            meta.setDestroyableKeys(keys)
        }

    override var placeableKeys: Set<Namespaced>
        get() = meta.placeableKeys
        set(keys) {
            meta.setPlaceableKeys(keys)
        }

    override fun lore(vararg component: Component): B {
        meta.lore(component.asList())
        return this as B
    }

    override fun addEnchant(vararg enchantments: Pair<Enchantment, Int>): B {
        enchantments.forEach { meta.addEnchant(it.first, it.second, true) }
        return this as B
    }

    override fun removeEnchant(vararg enchantments: Enchantment): B {
        enchantments.filter(meta::hasEnchant).forEach(meta::removeEnchant)
        return this as B
    }

    override fun addFlag(vararg flags: ItemFlag): B {
        meta.addItemFlags(*flags)
        return this as B
    }

    override fun removeFlag(vararg flags: ItemFlag): B {
        meta.removeItemFlags(*flags)
        return this as B
    }

    override fun Attribute.addModifier(vararg modifiers: AttributeModifier): B {
        modifiers.forEach { meta.addAttributeModifier(this, it) }
        return this@BaseItemBuilderImpl as B
    }

    override fun Attribute.removeModifier(vararg modifiers: AttributeModifier): B {
        modifiers.forEach { meta.removeAttributeModifier(this, it) }
        return this@BaseItemBuilderImpl as B
    }

    override fun Attribute.clearModifiers(): B {
        meta.removeAttributeModifier(this)
        return this@BaseItemBuilderImpl as B
    }

    override fun pdc(block: PDC.() -> Unit): B {
        meta.pdc.apply(block)
        return this as B
    }

    override fun build(): ItemStack {
        itemStack.itemMeta = meta
        return itemStack
    }
}
