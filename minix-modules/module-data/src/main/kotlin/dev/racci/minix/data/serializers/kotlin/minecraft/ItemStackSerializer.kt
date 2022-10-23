package dev.racci.minix.data.serializers.kotlin.minecraft

import dev.racci.minix.data.exceptions.SerializationException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import kotlin.properties.Delegates

public object ItemStackSerializer : KSerializer<ItemStack> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ItemStack") {
        element<Material>("type")
        element<Int>("amount", isOptional = true)
        element<ItemMetaSerializer>("meta", isOptional = true)
    }

    override fun serialize(
        encoder: Encoder,
        value: ItemStack
    ) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.type.name)
            if (value.amount > 1) encodeIntElement(descriptor, 1, value.amount)
            if (value.hasItemMeta()) encodeSerializableElement(descriptor, 2, ItemMetaSerializer, value.itemMeta)
        }
    }

    override fun deserialize(decoder: Decoder): ItemStack {
        var type: Material by Delegates.notNull()
        var amount = 1
        var meta: ItemMeta? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val i = decodeElementIndex(descriptor)) {
                    0 -> runCatching { type = Material.valueOf(decodeStringElement(descriptor, i)) }.onFailure { throw SerializationException(descriptor, i) }
                    1 -> amount = decodeIntElement(descriptor, i)
                    2 -> meta = decodeSerializableElement(descriptor, i, ItemMetaSerializer)
                    CompositeDecoder.DECODE_DONE -> break
                }
            }
        }

        return ItemStack(type).apply {
            this.amount = amount
            if (this.itemMeta != null) this.itemMeta = meta
        }
    }
}
