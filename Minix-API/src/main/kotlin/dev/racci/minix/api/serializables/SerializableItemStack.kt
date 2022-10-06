package dev.racci.minix.api.serializables

import com.destroystokyo.paper.profile.CraftPlayerProfile
import com.mojang.authlib.GameProfile
import dev.racci.minix.api.extensions.addTexture
import dev.racci.minix.api.extensions.collections.toMultiMap
import dev.racci.minix.api.extensions.editItemMeta
import dev.racci.minix.api.utils.collections.MultiMap
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.util.UUID

/**
 * A wrapper for [ItemStack] that uses [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization).
 * Allows for easy-to-use serialization to JSON (or YAML with kml).
 *
 * Currently, missing many things spigot's item serialization contains, but way cleaner to use!
 */
@Serializable
@ConfigSerializable
class SerializableItemStack(
    @SerialName("Type") var type: Material? = null,
    @SerialName("Amount") var amount: Int = 1,
    @SerialName("Meta") var meta: SerializableItemMeta? = null,
    @SerialName("SkullMeta") var skullMeta: SerializableSkullMeta? = null,
    @SerialName("Tag") var tag: String = ""
//    @SerialName("PDC") var tag: ImmutableMap<
//          @Serializable(with = NamespacedKeySerializer::class) NamespacedKey, String>? = null,
) {

    fun toItemStack(item: ItemStack = ItemStack(type ?: Material.AIR)): ItemStack {
        item.amount = amount
        if (meta != null) {
            val meta = meta!!
            item.editItemMeta<ItemMeta> {
                displayName(meta.displayName)
                lore(meta.lore)
                setCustomModelData(meta.customModelData)
                meta.enchants?.entries?.forEach { (enchant, level) ->
                    addEnchant(enchant, level, true)
                }
                meta.itemFlags?.forEach { addItemFlags(it) }
                isUnbreakable = meta.unbreakable
                meta.attributeModifiers?.entries?.forEach { (attribute, modifiers) ->
                    modifiers?.forEach { addAttributeModifier(attribute, it) }
                }
                meta.damage?.let { (this as? Damageable)?.damage = it }
            }
        }
        return item
    }
}

fun SerializableItemStack.Companion.dsl(block: SerializableItemStack.() -> Unit): SerializableItemStack {
    val serializableItemStack = SerializableItemStack()
    serializableItemStack.block()
    return serializableItemStack
}

@Serializable
@ConfigSerializable
class SerializableItemMeta(
    @SerialName("DisplayName")
    var displayName: Component? = null,
    @SerialName("LocalizedName")
    var localizedName: String? = null,
    @SerialName("Lore")
    var lore: ImmutableList<Component>? = null,
    @SerialName("CustomModelData")
    var customModelData: Int? = null,
    @SerialName("Enchants")
    var enchants: ImmutableMap<@Serializable(with = EnchantSerializer::class) Enchantment, Int>? = null,
    @SerialName("ItemFlags")
    var itemFlags: ImmutableSet<ItemFlag>? = null,
    @SerialName("Unbreakable")
    var unbreakable: Boolean = false,
    @SerialName("AttributeModifiers")
    var attributeModifiers: MultiMap<Attribute,
        /*@Serializable(with = AttributeModifierSerializer::class)*/ @Contextual AttributeModifier>? = null,
    @SerialName("Damage")
    var damage: Int? = null
)

fun SerializableItemMeta.Companion.dsl(block: SerializableItemMeta.() -> Unit): SerializableItemMeta {
    val serializableItemMeta = SerializableItemMeta()
    serializableItemMeta.block()
    return serializableItemMeta
}

@Serializable
@ConfigSerializable
class SerializableSkullMeta {

    @[SerialName("Owner") Serializable(with = UUIDSerializer::class)]
    var owner: UUID? = null

    @SerialName("Texture")
    var texture: String? = null

    @Suppress("DEPRECATION")
    fun applyTo(meta: ItemMeta) {
        if (meta !is SkullMeta) return
        if (meta.playerProfile == null) {
            meta.playerProfile = CraftPlayerProfile(GameProfile(owner ?: UUID.randomUUID(), null))
        } else meta.playerProfile!!.id = owner ?: UUID.randomUUID()
        meta.addTexture(texture)
    }
}

fun SerializableSkullMeta.Companion.dsl(block: SerializableSkullMeta.() -> Unit): SerializableSkullMeta {
    val serializableSkullMeta = SerializableSkullMeta()
    serializableSkullMeta.block()
    return serializableSkullMeta
}

/**
 * Converts an [ItemStack] to [SerializableItemStack].
 *
 * @see SerializableItemStack
 */
fun ItemStack.toSerializable(): SerializableItemStack {
    val itemMeta = itemMeta
    return SerializableItemStack.dsl {
        type = this@toSerializable.type
        amount = this@toSerializable.amount
        meta = SerializableItemMeta.dsl {
            displayName = itemMeta.displayName()
            lore = itemMeta.lore()?.toImmutableList()
            customModelData = itemMeta.customModelData
            enchants = itemMeta.enchants.toImmutableMap()
            itemFlags = itemMeta.itemFlags.toImmutableSet()
            unbreakable = itemMeta.isUnbreakable
            attributeModifiers = itemMeta.attributeModifiers?.toMultiMap()
            if (itemMeta is Damageable) {
                damage = itemMeta.damage
            }
        }
//        if(itemMeta is SkullMeta) {
//            skullMeta = SerializableSkullMeta.dsl {
//                owner = itemMeta.owningPlayer?.uniqueId
//                texture = itemMeta.texture
//            }
//        }
    }
}
