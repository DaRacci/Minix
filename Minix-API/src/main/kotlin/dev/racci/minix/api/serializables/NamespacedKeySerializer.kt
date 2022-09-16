package dev.racci.minix.api.serializables

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.NamespacedKey
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

object NamespacedKeySerializer : KSerializer<NamespacedKey> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NamespacedKey", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: NamespacedKey
    ) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): NamespacedKey = NamespacedKey.fromString(decoder.decodeString())!!

    object Configurate : TypeSerializer<NamespacedKey> {

        override fun serialize(
            type: Type,
            obj: NamespacedKey?,
            node: ConfigurationNode
        ) {
            if (obj == null) {
                node.raw(null); return
            }
            node.set(obj.toString())
        }

        override fun deserialize(
            type: Type,
            node: ConfigurationNode
        ): NamespacedKey = node.get<String>()?.let(NamespacedKey::fromString) ?: error("Null or invalid NamespacedKey: ${node.get<String>()}")
    }
}
