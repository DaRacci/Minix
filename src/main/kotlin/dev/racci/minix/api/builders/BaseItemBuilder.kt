package dev.racci.minix.api.builders

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
import org.jetbrains.annotations.ApiStatus

@ApiStatus.AvailableSince("0.1.5")
@Suppress("ComplexInterface")
interface BaseItemBuilder<B : BaseItemBuilder<B, M>, M : ItemMeta> {

    var itemStack: ItemStack

    var meta: M

    /**
     * Get or set the amount of items in the stack.
     */
    var amount: Int

    /**
     * Get or set the name of the item.
     */
    var name: Component?

    /**
     * Get or set the lore of the item.
     */
    var lore: List<Component>

    /**
     * Get or set the model of the item.
     */
    var model: Int?

    /**
     * Get or add to these items flags.
     */
    var itemFlags: Set<ItemFlag>

    /**
     * Get or set the [ItemStack.isUnbreakable] status of this item.
     */
    var isUnbreakable: Boolean

    /**
     * A glowing item is an item with a dummy enchant like unbreaking 1 and
     * the hide enchants flag.
     * ## Warning this will override any enchants on your item.
     */
    var glowing: Boolean

    /**
     * Gets or sets this items attribute modifiers.
     */
    var attributeModifiers: Multimap<Attribute, AttributeModifier>?

    /**
     * Gets or sets these items destroyable keys.
     */
    var destroyableKeys: Set<Namespaced>

    /**
     * Gets or sets these items placeable keys.
     */
    var placeableKeys: Set<Namespaced>

    /**
     * Set the lore of the item.
     */
    fun lore(vararg component: Component): B

    /**
     * Adds the enchants to this item with the paired level.
     */
    fun addEnchant(vararg enchantments: Pair<Enchantment, Int>): B

    /**
     * Removes the enchants from this item.
     */
    fun removeEnchant(vararg enchantments: Enchantment): B

    /**
     * Adds an item flag to this item.
     */
    fun addFlag(vararg flags: ItemFlag): B

    /**
     * Removes an item flag from this item.
     */
    fun removeFlag(vararg flags: ItemFlag): B

    /**
     * Creates a new set of attribute modifiers for this attribute on the item.
     */
    fun Attribute.addModifier(vararg modifiers: AttributeModifier): B

    /**
     * Removes an attribute modifier of the attribute from this item.
     */
    fun Attribute.removeModifier(vararg modifiers: AttributeModifier): B

    /**
     * Clears all modifiers of this attribute type from the item.
     */
    fun Attribute.clearModifiers(): B

    /**
     * Provides a block to modify these items Persistent Data Container.
     */
    fun pdc(block: PDC.() -> Unit): B

    /**
     * Builds the final item.
     */
    fun build(): ItemStack
}
