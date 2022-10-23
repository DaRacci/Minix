package dev.racci.minix.data.serializers.kotlin.minecraft

import dev.racci.minix.api.utils.collections.muiltimap.MultiMap
import dev.racci.minix.data.serializers.kotlin.MiniMessageSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer

// TODO
public object ItemMetaSerializer : KSerializer<ItemMeta> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ItemMeta") {
        element<MiniMessageSerializer>("name", isOptional = true)
        element<Array<MiniMessageSerializer>>("lore", isOptional = true)

        element<Int>("model", isOptional = true)
        element<Boolean>("unbreakable", isOptional = true)
        element<Boolean>("hide-enchants", isOptional = true)
        element<Boolean>("hide-attributes", isOptional = true)
        element<Boolean>("hide-unbreakable", isOptional = true)
        element<Boolean>("hide-destroys", isOptional = true)
        element<Boolean>("hide-placed-on", isOptional = true)
        element<Boolean>("hide-potions", isOptional = true)
        element<Boolean>("hide-dye", isOptional = true)

        element<Array<EnchantSerializer>>("enchants", isOptional = true)
        element<MultiMap<Attribute, AttributeModifierSerializer>>("attributes", isOptional = true)
        element<PersistentDataContainer>("data", isOptional = true)

        element<Int>("damage", isOptional = true)
    }

    override fun deserialize(
        decoder: Decoder
    ): ItemMeta = ItemStack(Material.BARRIER).itemMeta

    override fun serialize(
        encoder: Encoder,
        value: ItemMeta
    ) {
    }
}
