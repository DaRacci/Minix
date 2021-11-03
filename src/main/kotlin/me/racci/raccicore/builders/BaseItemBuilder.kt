package me.racci.raccicore.builders

import me.racci.raccicore.utils.items.ItemNBT
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import java.util.function.Function

@Suppress("UNCHECKED_CAST")
abstract class BaseItemBuilder<T : BaseItemBuilder<T>> protected constructor(var itemStack: ItemStack) {

    var meta : ItemMeta = if (itemStack.hasItemMeta()) itemStack.itemMeta else Bukkit.getItemFactory().getItemMeta(itemStack.type)

    var name
        get() = meta.displayName()
        set(component) {meta.displayName(component)}
    open fun name(component: Component) : T {
        meta.displayName(component)
        return this as T
    }

    var lore: Component
        get() = throw UnsupportedOperationException()
        set(component) {meta.lore(listOf(component))}
    open fun lore(vararg component: Component) : T {
        meta.lore(component.asList())
        return this as T
    }

    open fun lore(unit: List<Component>.() -> List<Component>) {
        meta.lore(unit.invoke(meta.lore() ?: emptyList()))
    }

    @Deprecated("I mean tbh just use the lambada", ReplaceWith("lore(unit: List<Component>.() -> List<Component>))"))
    open fun lore(component: Function<List<Component>, List<Component>>) : T {
        meta.lore(component.apply(meta.lore() ?: emptyList()))
        return this as T
    }

    open fun amount(amount: Int) : T {
        itemStack.amount = amount
        return this as T
    }

    open fun enchant(vararg enchants: Pair<Enchantment, Int>) : T {
        enchants.forEach{meta.addEnchant(it.first, it.second, true)}
        return this as T
    }

    open fun disenchant(vararg enchants: Enchantment) : T {
        enchants.filter(meta::hasEnchant).forEach(meta::removeEnchant)
        return this as T
    }

    open fun addFlag(vararg flags: ItemFlag) : T {
        meta.addItemFlags(*flags)
        return this as T
    }

    open fun removeFlag(vararg flags: ItemFlag) : T {
        meta.removeItemFlags(*flags)
        return this as T
    }

    open fun unbreakable(unbreakable: Boolean = true) : T {
        meta.isUnbreakable = unbreakable
        return this as T
    }

    open fun glow() : T {
        if(!meta.hasEnchants()) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
        return this as T
    }

    open fun model(modelData: Int) : T {
        meta.setCustomModelData(modelData)
        return this as T
    }

    open fun pdc(unit: PersistentDataContainer.() -> Unit) : T {
        unit(meta.persistentDataContainer)
        return this as T
    }

    open fun stringNBT(vararg pair: Pair<String, String>) : T {
        itemStack.itemMeta = meta
        pair.forEach{ItemNBT.setString(itemStack, it.first, it.second)}
        meta = itemStack.itemMeta
        return this as T
    }

    open fun booleanNBT(vararg pair: Pair<String, Boolean>) : T {
        itemStack.itemMeta = meta
        pair.forEach{ItemNBT.setBoolean(itemStack, it.first, it.second)}
        meta = itemStack.itemMeta
        return this as T
    }

    open fun removeNBT(vararg key: String) : T {
        itemStack.itemMeta = meta
        key.forEach{ItemNBT.removeTag(itemStack, it)}
        meta = itemStack.itemMeta
        return this as T
    }

    open fun build() : ItemStack {
        itemStack.itemMeta = meta
        return itemStack
    }

}