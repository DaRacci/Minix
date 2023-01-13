package dev.racci.minix.api.paper.builders

import com.destroystokyo.paper.Namespaced
import com.google.common.collect.Multimap
import dev.racci.minix.api.extensions.PDC
import net.kyori.adventure.text.Component
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

public interface BaseItemBuilder<B : BaseItemBuilder<B, M>, M : ItemMeta> {

    public var itemStack: ItemStack

    public var meta: M

    /**
     * Get or set the number of items in the stack.
     */
    public var amount: Int

    /**
     * Get or set the name of the item.
     */
    public var name: Component?

    /**
     * Get or set the lore of the item.
     */
    public var lore: List<Component>

    /**
     * Get or set the model of the item.
     */
    public var model: Int?

    /**
     * Get or add to these item flags.
     */
    public var itemFlags: Set<ItemFlag>

    /**
     * Get or set the [ItemStack.isUnbreakable] status of this item.
     */
    public var isUnbreakable: Boolean

    /**
     * Makes this item glow by applying [Enchantment.DURABILITY] and [ItemFlag.HIDE_ENCHANTS]
     * ## Warning this will override any enchants on your item.
     */
    public var glowing: Boolean

    /**
     * Gets or sets this items attribute modifiers.
     */
    public var attributeModifiers: Multimap<Attribute, AttributeModifier>?

    /**
     * Gets or sets these items destroyable keys.
     */
    public var destroyableKeys: Set<Namespaced>

    /**
     * Gets or sets these items placeable keys.
     */
    public var placeableKeys: Set<Namespaced>

    /**
     * Set the lore of the item.
     */
    public fun lore(vararg component: Component): B

    /**
     * Adds the enchants to this item with the paired level.
     */
    public fun addEnchant(vararg enchantments: Pair<Enchantment, Int>): B

    /**
     * Removes the enchants from this item.
     */
    public fun removeEnchant(vararg enchantments: Enchantment): B

    /**
     * Adds an item flag to this item.
     */
    public fun addFlag(vararg flags: ItemFlag): B

    /**
     * Removes an item flag from this item.
     */
    public fun removeFlag(vararg flags: ItemFlag): B

    /**
     * Creates a new set of attribute modifiers for this attribute on the item.
     */
    public fun Attribute.addModifier(vararg modifiers: AttributeModifier): B

    /**
     * Removes an attribute modifier of the attribute from this item.
     */
    public fun Attribute.removeModifier(vararg modifiers: AttributeModifier): B

    /**
     * Clears all modifiers of this attribute type from the item.
     */
    public fun Attribute.clearModifiers(): B

    /**
     * Provides a block to modify these items Persistent Data Container.
     */
    public fun pdc(block: PDC.() -> Unit): B

    /**
     * Builds the final item.
     */
    public fun build(): ItemStack
}
