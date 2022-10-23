package dev.racci.minix.data.serializers.configurate

import dev.racci.minix.data.DataSize
import dev.racci.minix.data.serializers.base.DataSerializerBase
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

public object ConfigurateDataSerializer : DataSerializerBase, TypeSerializer<DataSize> {
    override fun deserialize(
        type: Type,
        node: ConfigurationNode
    ): DataSize {
        val match = node.get<String>()?.let(DataSize.regex::matchEntire)
        return super.deserialize(match)
    }

    override fun serialize(
        type: Type,
        obj: DataSize?,
        node: ConfigurationNode
    ) {
        if (obj == null) { node.raw(null); return }
        node.set(super.serialize(obj))
    }
}
