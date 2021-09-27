package me.racci.raccicore.utils.items

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin


object ItemNBT {
    private val nbt: NbtWrapper = selectNbt()

    /**
     * Sets an NBT tag to the an [ItemStack].
     *
     * @param itemStack The current [ItemStack] to be set.
     * @param key       The NBT key to use.
     * @param value     The tag value to set.
     * @return An [ItemStack] that has NBT set.
     */
    fun setString(itemStack: ItemStack, key: String, value: String): ItemStack? {
        return nbt.setString(itemStack, key, value)
    }

    /**
     * Gets the NBT tag based on a given key.
     *
     * @param itemStack The [ItemStack] to get from.
     * @param key       The key to look for.
     * @return The tag that was stored in the [ItemStack].
     */
    fun getString(itemStack: ItemStack, key: String): String? {
        return nbt.getString(itemStack, key)
    }

    /**
     * Sets a boolean to the [ItemStack].
     * Mainly used for setting an item to be unbreakable on older versions.
     *
     * @param itemStack The [ItemStack] to set the boolean to.
     * @param key       The key to use.
     * @param value     The boolean value.
     * @return An [ItemStack] with a boolean value set.
     */
    fun setBoolean(itemStack: ItemStack, key: String, value: Boolean): ItemStack? {
        return nbt.setBoolean(itemStack, key, value)
    }

    /**
     * Removes a tag from an [ItemStack].
     *
     * @param itemStack The current [ItemStack] to be removed.
     * @param key       The NBT key to remove.
     * @return An [ItemStack] that has the tag removed.
     */
    fun removeTag(itemStack: ItemStack, key: String): ItemStack? {
        return nbt.removeTag(itemStack, key)
    }

    private fun selectNbt(): NbtWrapper {
        return Pdc()
    }
}

interface NbtWrapper {
    /**
     * Sets a String NBT tag to the an [ItemStack].
     *
     * @param itemStack The current [ItemStack] to be set.
     * @param key       The NBT key to use.
     * @param value     The tag value to set.
     * @return An [ItemStack] that has NBT set.
     */
    fun setString(itemStack: ItemStack, key: String?, value: String?): ItemStack?

    /**
     * Removes a tag from an [ItemStack].
     *
     * @param itemStack The current [ItemStack] to be removed.
     * @param key       The NBT key to remove.
     * @return An [ItemStack] that has the tag removed.
     */
    fun removeTag(itemStack: ItemStack, key: String?): ItemStack?

    /**
     * Sets a boolean to the [ItemStack].
     * Mainly used for setting an item to be unbreakable on older versions.
     *
     * @param itemStack The [ItemStack] to set the boolean to.
     * @param key       The key to use.
     * @param value     The boolean value.
     * @return An [ItemStack] with a boolean value set.
     */
    fun setBoolean(itemStack: ItemStack, key: String?, value: Boolean): ItemStack?

    /**
     * Gets the NBT tag based on a given key.
     *
     * @param itemStack The [ItemStack] to get from.
     * @param key       The key to look for.
     * @return The tag that was stored in the [ItemStack].
     */
    fun getString(itemStack: ItemStack, key: String?): String?
}
class Pdc : NbtWrapper {
    /**
     * Sets a String NBT tag to the an [ItemStack].
     *
     * @param itemStack The current [ItemStack] to be set.
     * @param key       The NBT key to use.
     * @param value     The tag value to set.
     * @return An [ItemStack] that has NBT set.
     */
    override fun setString(itemStack: ItemStack, key: String?, value: String?): ItemStack {
        val meta = itemStack.itemMeta ?: return itemStack
        meta.persistentDataContainer.set(NamespacedKey(PLUGIN, key!!), PersistentDataType.STRING, value!!)
        itemStack.itemMeta = meta
        return itemStack
    }

    /**
     * Removes a tag from an [ItemStack].
     *
     * @param itemStack The current [ItemStack] to be removed.
     * @param key       The NBT key to remove.
     * @return An [ItemStack] that has the tag removed.
     */
    override fun removeTag(itemStack: ItemStack, key: String?): ItemStack {
        val meta = itemStack.itemMeta ?: return itemStack
        meta.persistentDataContainer.remove(NamespacedKey(PLUGIN, key!!))
        itemStack.itemMeta = meta
        return itemStack
    }

    /**
     * Sets a boolean to the [ItemStack].
     * Mainly used for setting an item to be unbreakable on older versions.
     *
     * @param itemStack The [ItemStack] to set the boolean to.
     * @param key       The key to use.
     * @param value     The boolean value.
     * @return An [ItemStack] with a boolean value set.
     */
    override fun setBoolean(itemStack: ItemStack, key: String?, value: Boolean): ItemStack {
        val meta = itemStack.itemMeta ?: return itemStack
        meta.persistentDataContainer.set(NamespacedKey(PLUGIN, key!!),
            PersistentDataType.BYTE,
            if (value) 1.toByte() else 0)
        itemStack.itemMeta = meta
        return itemStack
    }

    /**
     * Gets the NBT tag based on a given key.
     *
     * @param itemStack The [ItemStack] to get from.
     * @param key       The key to look for.
     * @return The tag that was stored in the [ItemStack].
     */
    override fun getString(itemStack: ItemStack, key: String?): String? {
        val meta = itemStack.itemMeta ?: return null
        return meta.persistentDataContainer.get(NamespacedKey(PLUGIN, key!!), PersistentDataType.STRING)
    }

    companion object {
        /**
         * Plugin instance required for the [NamespacedKey].
         */
        private val PLUGIN: Plugin = JavaPlugin.getProvidingPlugin(Pdc::class.java)
    }
}