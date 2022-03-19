package dev.racci.minix.api.serializables

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type
import org.spongepowered.configurate.kotlin.extensions.get

object EnchantSerializer : KSerializer<Enchantment>, TypeSerializer<Enchantment> {

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

    override fun serialize(
        type: Type,
        obj: Enchantment?,
        node: ConfigurationNode,
    ) {
        if (obj == null) { node.raw(null); return }
        node.set(obj.key.toString())
    }

    override fun deserialize(
        type: Type,
        node: ConfigurationNode,
    ): Enchantment = node.get<String>()?.let(::fromString) ?: error("Cannot deserialize null.")

    // TODO: Support EcoEnchants
    private fun fromString(value: String): Enchantment {
        val key = NamespacedKey.fromString(value)
            ?: error("Namespaced key cannot result in null.")
        return Enchantment.getByKey(key)
            ?: error("NamespacedKey must be a valid enchantment key.")
    }
}
