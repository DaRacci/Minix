package me.racci.raccilib.utils.items.builders

import me.racci.raccilib.utils.items.ItemNBT
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.jetbrains.annotations.Contract
import org.jetbrains.annotations.Nullable
import java.util.function.Consumer
import java.util.stream.Collectors


abstract class BaseItemBuilder<B : BaseItemBuilder<B>> protected constructor(itemStack: ItemStack) {

    private var itemStack: ItemStack
    private var meta: ItemMeta

    init {
        this.itemStack = itemStack
        this.meta = if (itemStack.hasItemMeta()) itemStack.itemMeta else Bukkit.getItemFactory().getItemMeta(itemStack.type)
    }

    @Contract("_ -> this")
    open fun name(name: Component): B {
        meta.displayName(name)
        return this as B
    }

    open fun amount(amount: Int): B {
        itemStack.amount = amount
        return this as B
    }

    @Contract("_ -> this")
    open fun lore(vararg lore: Component): B {
        return lore(lore.asList())
    }

    @Contract("_ -> this")
    open fun lore(lore: List<Component>): B {
        // meta.lore(lore.stream().map(Legacy.SERIALIZER::serialize).collect(Collectors.toList()))
        meta.lore(lore.stream().collect(Collectors.toList()))
        return this as B
    }

    @Contract("_ -> this")
    open fun lore(lore: Consumer<List<Component>>): B {
        var metaLore = meta.lore()
        if (metaLore == null) {
            metaLore = ArrayList()
        }
        lore.accept(metaLore)
        return lore(metaLore)
    }

    @Contract("_, _, _ -> this")
    open fun enchant(enchantment: Enchantment, level: Int, ignoreLevelRestriction: Boolean): B {
        meta.addEnchant(enchantment, level, ignoreLevelRestriction)
        return this as B
    }

    @Contract("_, _ -> this")
    open fun enchant(enchantment: Enchantment, level: Int): B {
        return enchant(enchantment, level, true)
    }

    @Contract("_ -> this")
    open fun enchant(enchantment: Enchantment): B {
        return enchant(enchantment, 1, true)
    }

    @Contract("_ -> this")
    open fun disenchant(enchantment: Enchantment): B {
        itemStack.removeEnchantment(enchantment)
        return this as B
    }

    @Contract("_ -> this")
    open fun flags(vararg flags: ItemFlag): B {
        meta.addItemFlags(*flags)
        return this as B
    }

    @Contract(" -> this")
    open fun unbreakable(): B {
        return unbreakable(true)
    }

    @Contract("_ -> this")
    open fun unbreakable(unbreakable: Boolean): B {
        meta.isUnbreakable = unbreakable
        return this as B
    }

    @Contract(" -> this")
    open fun glow(): B {
        return glow(true)
    }

    @Contract("_ -> this")
    open fun glow(glow: Boolean): B {
        if (glow) {
            meta.addEnchant(Enchantment.LURE, 1, false)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            return this as B
        }
        for (enchantment in meta.enchants.keys) {
            meta.removeEnchant(enchantment!!)
        }
        return this as B
    }

    @Contract("_ -> this")
    open fun pdc(consumer: Consumer<PersistentDataContainer?>): B {
        consumer.accept(meta.persistentDataContainer)
        return this as B
    }

    @Contract("_ -> this")
    open fun model(modelData: Int): B {
        return this as B
    }

    @Contract("_, _ -> this")
    open fun setNbt(key: String, @Nullable value: String?): B {
        itemStack.itemMeta = meta
        itemStack = ItemNBT.setString(itemStack, key, value!!)!!
        meta = itemStack.itemMeta
        return this as B
    }

    @Contract("_, _ -> this")
    open fun setNbt(key: String, value: Boolean): B {
        itemStack.itemMeta = meta
        itemStack = ItemNBT.setBoolean(itemStack, key, value)!!
        meta = itemStack.itemMeta
        return this as B
    }

    @Contract("_ -> this")
    open fun removeNbt(key: String): B {
        itemStack.itemMeta = meta
        itemStack = ItemNBT.removeTag(itemStack, key)!!
        meta = itemStack.itemMeta
        return this as B
    }

    open fun build(): ItemStack? {
        itemStack.itemMeta = meta
        return itemStack
    }

    protected open fun getItemStack(): ItemStack {
        return itemStack
    }

    protected open fun setItemStack(itemStack: ItemStack) {
        this.itemStack = itemStack
    }

    protected open fun getMeta(): ItemMeta {
        return meta
    }

    protected open fun setMeta(meta: ItemMeta) {
        this.meta = meta
    }
}