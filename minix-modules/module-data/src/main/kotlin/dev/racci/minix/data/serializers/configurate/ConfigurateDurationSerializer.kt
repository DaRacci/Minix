package dev.racci.minix.data.serializers.configurate

import dev.racci.minix.data.serializers.kotlin.DurationSerializer
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type
import kotlin.time.Duration

public object ConfigurateDurationSerializer : TypeSerializer<Duration> {
    override fun serialize(
        type: Type,
        obj: Duration?,
        node: ConfigurationNode
    ) {
        if (obj == null) { node.raw(null); return }
        node.set(DurationSerializer.toString(obj))
    }

    override fun deserialize(
        type: Type,
        node: ConfigurationNode
    ): Duration = node.get<String>()?.let(DurationSerializer::fromString) ?: error("Invalid Duration: ${node.get<String>()}")
}
