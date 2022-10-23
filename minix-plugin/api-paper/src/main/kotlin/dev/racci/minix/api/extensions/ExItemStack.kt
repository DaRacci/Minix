package dev.racci.minix.api.extensions

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataContainer

public typealias PDC = PersistentDataContainer

public val ItemStack.pdc: PDC get() = this.itemMeta.persistentDataContainer

public inline fun ItemStack.pdc(
    block: PDC.() -> Unit
): PDC = this.pdc.apply(block)

public inline fun <reified M : ItemMeta> ItemStack.editItemMeta(
    block: M.() -> Unit
): M? = (this.itemMeta as? M)?.apply(block)

public fun ItemStack.displayName(
    displayName: Component
): ItemMeta? = editItemMeta { displayName(displayName) }

// public fun ItemStack.addTexture(
//    texture: String?
// ): Unit = itemMeta.addTexture(texture)
//
// public fun ItemMeta.addTexture(
//    texture: String?
// ) {
//    val meta = this as? SkullMeta ?: return
//    val profile = GameProfile(meta.owningPlayer?.uniqueId ?: UUID.randomUUID(), null)
//    profile.properties.put("textures", Property("textures", texture))
//    unsafe {
//        danger {
//            val mtd = meta::class.java.getDeclaredMethod("setProfile", GameProfile::class.java)
//            mtd.isAccessible = true
//            mtd.invoke(meta, profile)
//        }
//        catch(IllegalAccessException::class, InvocationTargetException::class, NoSuchFieldException::class) {
//            getKoin().get<Minix>().log.debug { "Exception caught while putting textures on skull. $this" }
//        }
//    }
// }

public val SkullMeta.texture: String? get() =
    playerProfile?.properties?.firstOrNull { it.name == "textures" }?.value

public inline fun Material.asItemStack(
    amount: Int = 1,
    meta: ItemMeta.() -> Unit = {}
): ItemMeta? = ItemStack(this, amount).editItemMeta(meta)

public inline val Material.isPickaxe: Boolean get() = name.endsWith("PICKAXE")
public inline val Material.isSword: Boolean get() = name.endsWith("SWORD")
public inline val Material.isAxe: Boolean get() = name.endsWith("_AXE")
public inline val Material.isSpade: Boolean get() = name.endsWith("SPADE")
public inline val Material.isHoe: Boolean get() = name.endsWith("HOE")
public inline val Material.isOre: Boolean get() = name.endsWith("ORE")
public inline val Material.isIngot: Boolean get() = name.endsWith("INGOT")
public inline val Material.isDoor: Boolean get() = name.endsWith("DOOR")
public inline val Material.isMinecart: Boolean get() = name.endsWith("MINECART")
public inline val Material.isWater: Boolean get() = this == Material.WATER
public inline val Material.isLava: Boolean get() = this == Material.LAVA
