package dev.racci.minix.data.serializers.kotlin

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey

// TODO -> Error handling
public object KeySerializer : KSerializer<Key> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NamespacedKey", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Key
    ): Unit = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): NamespacedKey = NamespacedKey.fromString(decoder.decodeString())!!
}
