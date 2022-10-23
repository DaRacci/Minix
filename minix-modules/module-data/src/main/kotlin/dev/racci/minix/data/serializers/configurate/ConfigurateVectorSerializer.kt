package dev.racci.minix.data.serializers.configurate

import org.bukkit.util.Vector
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

public object ConfigurateVectorSerializer : TypeSerializer<Vector> {

    override fun serialize(
        type: Type,
        obj: Vector?,
        node: ConfigurationNode
    ) {
        if (obj == null) {
            node.raw(null); return
        }
        node.set(obj.serialize())
    }

    override fun deserialize(
        type: Type,
        node: ConfigurationNode
    ): Vector = node.get<Map<String, Any>>()?.let(Vector::deserialize) ?: throw SerializationException(type, "Cannot deserialize null")
}
