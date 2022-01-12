@file:Suppress("UNCHECKED_CAST", "UNUSED")
package dev.racci.minix.api.builders

import com.destroystokyo.paper.Namespaced
import com.google.common.collect.Multimap
import dev.racci.minix.api.annotations.MinixDsl
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

/**
 * Sealed class for all item builder types.
 */
@MinixDsl
sealed class BaseItemBuilder<B: BaseItemBuilder<B, M>, M: ItemMeta> constructor(
    internal var itemStack: ItemStack = Material.AIR.toItemStack(),
    internal var meta: M = itemStack.itemMeta as? M
        ?: throw IncorrectItemTypeException(
            "The meta type ${Class<M>::getName} and builder type" +
                " ${Class<B>::getName} is not valid for item type ${itemStack.type.name}"
        ),
) {

    /**
     * Get or set the amount of items in the stack.
     */
    var amount: Int
        get() = itemStack.amount
        set(amount) { itemStack.amount = amount }

    /**
     * Get or set the name of the item.
     */
    var name: Component?
        get() = meta.displayName()
        set(name) { meta.displayName(name) }

    /**
     * Get or set the lore of the item.
     */
    var lore: List<Component>
        get() = meta.lore().orEmpty()
        set(lore) { meta.lore(lore) }

    /**
     * Get or set the model of the item.
     */
    var model: Int?
        get() = if(meta.hasCustomModelData()) meta.customModelData else null
        set(model) = meta.setCustomModelData(model)

    /**
     * Get or add to this items flags.
     */
    var itemFlags: Set<ItemFlag>
        get() = meta.itemFlags
        set(flags) { addFlag(*flags.toTypedArray()) }

    /**
     * Get or set the [ItemStack.isUnbreakable] status of this item.
     */
    var isUnbreakable: Boolean
        get() = meta.isUnbreakable
        set(boolean) { meta.isUnbreakable = boolean }

    /**
     * A glowing item is an item with a dummy enchant like unbreaking 1 and
     * the hide enchants flag.
     * ## Warning this will override any enchants on your item.
     */
    var glowing: Boolean
        get() = meta.hasEnchants() && meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS)
        set(boolean) {
            if(boolean) {
                if(!meta.hasEnchants()) {
                    meta.addEnchant(Enchantment.DURABILITY, 1, false)
                }
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            } else {
                removeEnchant(*meta.enchants.keys.toTypedArray())
            }
        }

    /**
     * Gets or sets this items attribute modifiers.
     */
    var attributeModifiers: Multimap<Attribute, AttributeModifier>?
        get() = meta.attributeModifiers
        set(multiMap) { meta.attributeModifiers = multiMap }

    /**
     * Gets or sets this items destroyable keys.
     */
    var destroyableKeys: Set<Namespaced>
        get() = meta.destroyableKeys
        set(keys) { meta.setDestroyableKeys(keys) }

    /**
     * Gets or sets this items placeable keys.
     */
    var placeableKeys: Set<Namespaced>
        get() = meta.placeableKeys
        set(keys) { meta.setPlaceableKeys(keys) }

    /**
     * Set the lore of the item.
     */
    fun lore(vararg component: Component): B {
        meta.lore(component.asList())
        return this as B
    }

    /**
     * Adds the enchants to this item with the paired level.
     */
    fun addEnchant(vararg enchantments: Pair<Enchantment, Int>): B {
        enchantments.forEach { meta.addEnchant(it.first, it.second, true) }
        return this as B
    }

    /**
     * Removes the enchants from this item.
     */
    fun removeEnchant(vararg enchantments: Enchantment): B {
        enchantments.filter(meta::hasEnchant).forEach(meta::removeEnchant)
        return this as B
    }

    /**
     * Adds an item flag to this item.
     */
    fun addFlag(vararg flags: ItemFlag): B {
        meta.addItemFlags(*flags)
        return this as B
    }

    /**
     * Removes an item flag from this item.
     */
    fun removeFlag(vararg flags: ItemFlag): B {
        meta.removeItemFlags(*flags)
        return this as B
    }

    /**
     * Creates a new set of attribute modifiers for this attribute on the item.
     */
    fun Attribute.addModifier(vararg modifiers: AttributeModifier): B {
        modifiers.forEach { meta.addAttributeModifier(this, it) }
        return this@BaseItemBuilder as B
    }

    /**
     * Removes an attribute modifier of the attribute from this item.
     */
    fun Attribute.removeModifier(vararg modifiers: AttributeModifier): B {
        modifiers.forEach { meta.removeAttributeModifier(this, it) }
        return this@BaseItemBuilder as B
    }

    /**
     * Clears all modifiers of this attribute type from the item.
     */
    fun Attribute.clearModifiers(): B {
        meta.removeAttributeModifier(this)
        return this@BaseItemBuilder as B
    }

    /**
     * Provides a block to modify this items Persistent Data Container.
     */
    fun pdc(block: PDC.() -> Unit): B {
        meta.pdc.apply(block)
        return this as B
    }

    /**
     * Builds the final item.
     */
    fun build(): ItemStack {
        itemStack.itemMeta = meta
        return itemStack
    }
}
