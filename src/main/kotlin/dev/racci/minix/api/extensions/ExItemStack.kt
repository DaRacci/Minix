@file:Suppress("UNUSED", "StringLiteralDuplication")

package dev.racci.minix.api.extensions

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import dev.racci.minix.api.utils.UnsafeUtil.Companion.unsafe
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.core.Minix
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataContainer
import java.lang.reflect.InvocationTargetException
import java.util.UUID

typealias PDC = PersistentDataContainer

val ItemStack.pdc get() = this.persistentDataContainer

inline fun ItemStack.pdc(
    block: PDC.() -> Unit,
) = this.pdc.apply(block)

inline fun <reified M : ItemMeta> ItemStack.editItemMeta(
    block: M.() -> Unit
) = (this.itemMeta as? M)?.apply(block)

fun ItemStack.displayName(
    displayName: Component
) = editItemMeta<ItemMeta> { displayName(displayName) }

fun ItemStack.addTexture(
    texture: String?
) = itemMeta.addTexture(texture)

fun ItemMeta.addTexture(
    texture: String?
) {
    val meta = this as? SkullMeta ?: return
    val profile = GameProfile(meta.owningPlayer?.uniqueId ?: UUID.randomUUID(), null)
    profile.properties.put("textures", Property("textures", texture))
    unsafe {
        danger {
            val mtd = meta::class.java.getDeclaredMethod("setProfile", GameProfile::class.java)
            mtd.isAccessible = true
            mtd.invoke(meta, profile)
        }
        catch(IllegalAccessException::class, InvocationTargetException::class, NoSuchFieldException::class) {
            getKoin().get<Minix>().log.debug { "Exception caught while putting textures on skull. $this" }
        }
    }
}

val SkullMeta.texture: String?
    get() =
        playerProfile?.properties?.firstOrNull { it.name == "textures" }?.value

inline fun Material.asItemStack(
    amount: Int = 1,
    meta: ItemMeta.() -> Unit = {}
) = ItemStack(this, amount).editItemMeta(meta)

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
