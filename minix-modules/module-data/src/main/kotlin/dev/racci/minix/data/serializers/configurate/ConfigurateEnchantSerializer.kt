package dev.racci.minix.data.serializers.configurate

import dev.racci.minix.data.serializers.kotlin.minecraft.EnchantSerializer
import org.bukkit.enchantments.Enchantment
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

public object ConfigurateEnchantSerializer : TypeSerializer<Enchantment> {

    override fun serialize(
        type: Type,
        obj: Enchantment?,
        node: ConfigurationNode
    ) {
        if (obj == null) {
            node.raw(null); return
        }
        node.set(obj.key.toString())
    }

    override fun deserialize(
        type: Type,
        node: ConfigurationNode
    ): Enchantment = node.get<String>()?.let(EnchantSerializer::fromString) ?: error("Cannot deserialize null.")
}
