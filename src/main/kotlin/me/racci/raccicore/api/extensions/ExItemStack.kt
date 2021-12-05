@file:Suppress("unused")
package me.racci.raccicore.api.extensions

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.*

inline fun <reified T: ItemMeta> ItemStack.meta(
    block: T.() -> Unit
) = apply {itemMeta = (itemMeta as? T)?.apply(block) ?: itemMeta}

fun ItemStack.displayName(
    displayName: Component
) = meta<ItemMeta> {displayName(displayName)}

fun ItemStack.lore(
    lore: List<Component>
) = meta<ItemMeta> {lore(lore)}

inline fun Material.asItemStack(
    amount: Int = 1,
    meta: ItemMeta.() -> Unit = {}
) = ItemStack(this, amount).meta(meta)

val gameProfileClass: Class<*> by lazy {Class.forName("com.mojang.authlib.GameProfile")}
val propertyClass: Class<*> by lazy {Class.forName("com.mojang.authlib.properties.Property")}
val getPropertiesMethod: Method by lazy {gameProfileClass.getMethod("getProperties")}
val gameProfileConstructor: Constructor<out Any> by lazy {gameProfileClass.getConstructor(UUID::class.java, String::class.java)}
val propertyConstructor: Constructor<out Any> by lazy {propertyClass.getConstructor(String::class.java, String::class.java)}

inline val Material.isPickaxe: Boolean get() = name.endsWith("PICKAXE")
inline val Material.isSword: Boolean get() = name.endsWith("SWORD")
inline val Material.isAxe: Boolean get() = name.endsWith("_AXE")
inline val Material.isSpade: Boolean get() = name.endsWith("SPADE")
inline val Material.isHoe: Boolean get() = name.endsWith("HOE")
inline val Material.isOre: Boolean get() = name.endsWith("ORE")
inline val Material.isIngot: Boolean get() = name.endsWith("INGOT")
inline val Material.isDoor: Boolean get() = name.endsWith("DOOR")
inline val Material.isMinecart: Boolean get() = name.endsWith("MINECART")
inline val Material.isWater: Boolean get() = this == Material.WATER
inline val Material.isLava: Boolean get() = this == Material.LAVA