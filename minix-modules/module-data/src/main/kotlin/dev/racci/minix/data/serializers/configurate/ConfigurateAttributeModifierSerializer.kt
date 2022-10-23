package dev.racci.minix.data.serializers.configurate

import org.bukkit.attribute.AttributeModifier
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

public object ConfigurateAttributeModifierSerializer : TypeSerializer<AttributeModifier> {
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
