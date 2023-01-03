package dev.racci.minix.data.serializers.kotlin.minecraft

import dev.racci.minix.data.extensions.encodeIf
import dev.racci.minix.data.extensions.encodeNonDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.serializer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

public object ItemStackSerializer : KSerializer<ItemStack> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ItemStack") {
        element<Material>("type")
        element<Int>("amount", isOptional = true)
        element<ItemMetaSerializer>("meta", isOptional = true)
    }

    override fun serialize(
        encoder: Encoder,
        value: ItemStack
    ): Unit = encoder.encodeStructure(descriptor) {
        encodeStringElement(descriptor, 0, value.type.name)
        encodeNonDefault(descriptor, 1, value.amount, 1)
        encodeIf(descriptor, 2, value.itemMeta, predicate = value::hasItemMeta)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): ItemStack = decoder.decodeStructure(descriptor) {
        val type = decodeSerializableElement(descriptor, 0, serializer<Material>())
        val amount = decodeIntElement(descriptor, 1)
        val meta = decodeNullableSerializableElement(descriptor, 2, ItemMetaSerializer.nullable)

        require(amount > 0) { "ItemStack amount must be greater than 0" }

        ItemStack(type).apply {
            this.amount = amount
            if (itemMeta != null) this.itemMeta = meta
        }
    }
}
