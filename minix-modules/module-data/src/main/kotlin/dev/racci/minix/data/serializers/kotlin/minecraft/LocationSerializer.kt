package dev.racci.minix.data.serializers.kotlin.minecraft

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import org.bukkit.Location

public object LocationSerializer : KSerializer<Location> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Location") {
        // TODO: Possibly transform x,y,z into one string like "x;y;z"
        element<Double>("x")
        element<Double>("y")
        element<Double>("z")
        element<String>("world")
        // TODO: Possibly transform yaw,pitch into one string like "yaw;pitch"
        element<Float>("pitch", isOptional = true)
        element<Float>("yaw", isOptional = true)
    }

    override fun serialize(
        encoder: Encoder,
        value: Location
    ): Unit = encoder.encodeStructure(descriptor) {
        encodeDoubleElement(descriptor, 0, value.x)
        encodeDoubleElement(descriptor, 1, value.y)
        encodeDoubleElement(descriptor, 2, value.z)
        encodeStringElement(descriptor, 3, value.world?.name ?: error("No world found while serializing"))
        encodeFloatElement(descriptor, 4, value.pitch)
        encodeFloatElement(descriptor, 5, value.yaw)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Location = decoder.decodeStructure(descriptor) {
        val x = decodeDoubleElement(descriptor, 0)
        val y = decodeDoubleElement(descriptor, 1)
        val z = decodeDoubleElement(descriptor, 2)
        val world = decodeNullableSerializableElement(descriptor, 3, WorldSerializer.nullable)
        val pitch = decodeNullableSerializableElement(descriptor, 4, Float.serializer().nullable)
        val yaw = decodeNullableSerializableElement(descriptor, 5, Float.serializer().nullable)

        Location(world, x, y, z).apply {
            if (pitch != null) this.pitch = pitch
            if (yaw != null) this.yaw = yaw
        }
    }
}
