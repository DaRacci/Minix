@file:Suppress("Unused")

package dev.racci.minix.api.serializables

import java.lang.reflect.Type
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.util.Vector
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer

object VectorSerializer : KSerializer<Vector>, TypeSerializer<Vector> {

    private val serializer = ListSerializer(Double.serializer())
    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun serialize(
        encoder: Encoder,
        value: Vector,
    ) = with(value) {
        encoder.encodeSerializableValue(serializer, listOf(x, y, z))
    }

    override fun deserialize(decoder: Decoder): Vector {
        val (x, y, z) = decoder.decodeSerializableValue(serializer)
        return Vector(x, y, z)
    }

    override fun serialize(
        type: Type,
        obj: Vector?,
        node: ConfigurationNode,
    ) {
        if (obj == null) { node.raw(null); return }
        node.set(obj.serialize())
    }

    override fun deserialize(
        type: Type,
        node: ConfigurationNode,
    ): Vector = node.get<Map<String, Any>>()?.let(Vector::deserialize) ?: throw SerializationException(type, "Cannot deserialize null")
}
