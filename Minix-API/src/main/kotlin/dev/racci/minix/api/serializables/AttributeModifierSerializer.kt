package dev.racci.minix.api.serializables

import dev.racci.minix.api.utils.primitive.EnumUtils
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type
import java.util.UUID

object AttributeModifierSerializer : KSerializer<AttributeModifier> {

    override val descriptor = buildClassSerialDescriptor("AttributeModifier") {
        element<UUID>("UUID")
        element<String>("Name")
        element<Double>("Amount")
        element<AttributeModifier.Operation>("Operation")
        element<EquipmentSlot>("Slot")
    }

    override fun serialize(
        encoder: Encoder,
        value: AttributeModifier
    ) = encoder.encodeStructure(descriptor) {
        encodeSerializableElement(descriptor, 0, UUIDSerializer, value.uniqueId)
        encodeStringElement(descriptor, 1, value.name)
        encodeDoubleElement(descriptor, 2, value.amount)
        encodeStringElement(descriptor, 3, value.operation.name)
        if (value.slot != null) {
            encodeStringElement(descriptor, 4, value.slot!!.name)
        }
    }

    override fun deserialize(decoder: Decoder): AttributeModifier {
        var uuid: UUID? = null
        var name: String? = null
        var amount: Double? = null
        var operation: String? = null
        var slot: String? = null
        decoder.decodeStructure(PotionEffectSerializer.descriptor) {
            while (true) {
                when (val i = decodeElementIndex(PotionEffectSerializer.descriptor)) {
                    0 -> uuid = decodeSerializableElement(descriptor, i, UUIDSerializer)
                    1 -> name = decodeStringElement(descriptor, i)
                    2 -> amount = decodeDoubleElement(descriptor, i)
                    3 -> operation = decodeStringElement(descriptor, i)
                    4 -> slot = decodeStringElement(descriptor, i)
                    CompositeDecoder.DECODE_DONE -> break
                }
            }
        }
        return AttributeModifier(
            uuid ?: UUID.randomUUID(),
            name ?: error("Name cannot be null for AttributeModifier"),
            amount ?: error("Amount cannot be null for AttributeModifier"),
            EnumUtils.getByName<AttributeModifier.Operation>(operation)
                ?: error("Operation cannot be null for AttributeModifier"),
            EnumUtils.getByName<EquipmentSlot>(slot)
        )
    }

    object Configurate : TypeSerializer<AttributeModifier> {
        override fun serialize(
            type: Type,
            obj: AttributeModifier?,
            node: ConfigurationNode
        ) {
            if (obj == null) { node.raw(null); return }
            node.set(obj.serialize())
        }

        override fun deserialize(
            type: Type,
            node: ConfigurationNode
        ): AttributeModifier = node.get<Map<String, Any>>()?.let(AttributeModifier::deserialize) ?: throw SerializationException(type, "AttributeModifier cannot be null")
    }
}
