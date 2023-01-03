package dev.racci.minix.data.serializers.kotlin.minecraft

import dev.racci.minix.data.extensions.encodeNonNull
import dev.racci.minix.data.serializers.kotlin.UUIDSerializer
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
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.EquipmentSlot
import java.util.UUID

public object AttributeModifierSerializer : KSerializer<AttributeModifier> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("AttributeModifier") {
        element<UUID>("UUID", isOptional = true)
        element<String>("Name")
        element<Double>("Amount")
        element<Operation>("Operation")
        element<EquipmentSlot>("Slot", isOptional = true)
    }

    override fun serialize(
        encoder: Encoder,
        value: AttributeModifier
    ): Unit = encoder.encodeStructure(descriptor) {
        encodeSerializableElement(descriptor, 0, UUIDSerializer, value.uniqueId)
        encodeStringElement(descriptor, 1, value.name)
        encodeDoubleElement(descriptor, 2, value.amount)
        encodeSerializableElement(descriptor, 3, serializer(), value.operation)
        encodeNonNull(descriptor, 4, serializer(), value.slot)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): AttributeModifier = decoder.decodeStructure(descriptor) {
        val uuid = decodeNullableSerializableElement(descriptor, 0, UUIDSerializer.nullable)
        val name = decodeStringElement(descriptor, 1)
        val amount = decodeDoubleElement(descriptor, 2)
        val operation = decodeSerializableElement(descriptor, 3, serializer<Operation>())
        val slot = decodeNullableSerializableElement(descriptor, 4, serializer<EquipmentSlot>().nullable)

        AttributeModifier(
            uuid ?: UUID.randomUUID(),
            name,
            amount,
            operation,
            slot
        )
    }
}
