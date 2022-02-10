package dev.racci.minix.api.serializables

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

object EnchantSerializer : KSerializer<Enchantment> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Enchantment", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Enchantment,
    ) = encoder.encodeString(value.key.toString())

    override fun deserialize(decoder: Decoder): Enchantment {
        val key = NamespacedKey.fromString(decoder.decodeString())
            ?: error("Namespaced key cannot result in null.")
        return Enchantment.getByKey(key)
            ?: error("NamespacedKey must be a valid enchantment key.")
    }
}
