package dev.racci.minix.data.serializers.configurate

import org.bukkit.NamespacedKey
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

public object ConfigurateNamespacedKeySerializer : TypeSerializer<NamespacedKey> {

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
