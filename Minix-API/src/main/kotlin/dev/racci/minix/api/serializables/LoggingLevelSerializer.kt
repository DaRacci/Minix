package dev.racci.minix.api.serializables

import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type
import java.util.logging.Level

object LoggingLevelSerializer : TypeSerializer<Level> {

    override fun deserialize(
        type: Type,
        node: ConfigurationNode,
    ): Level = node.get<String>()?.let(Level::parse) ?: throw SerializationException(type, "Invalid logging level: ${node.get<String>()}")

    override fun serialize(
        type: Type,
        obj: Level?,
        node: ConfigurationNode,
    ) {
        if (obj == null) { node.raw(null); return }
        node.set(obj.name)
    }
}
