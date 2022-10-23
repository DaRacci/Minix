package dev.racci.minix.data.serializers.kotlin.minecraft

import dev.racci.minix.data.structs.minecraft.BlockPos
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

public object BlockPosSerializer : TypeSerializer<BlockPos> {
    override fun deserialize(
        type: Type?,
        node: ConfigurationNode?
    ): BlockPos {
        if (node == null || node.string == null) throw SerializationException("Cannot deserialize null blockPos")

        val (x, y, z) = node.string!!
            .replace(' ', Character.MIN_VALUE)
            .split(';', limit = 3)
            .map { it.toDoubleOrNull() ?: throw SerializationException("Couldn't convert $it to a double.") }

        return BlockPos(x, y, z)
    }

    override fun serialize(
        type: Type?,
        obj: BlockPos?,
        node: ConfigurationNode
    ) {
        if (obj == null) {
            node.raw(null)
            return
        }

        node.raw("${obj.x};${obj.y};${obj.z}")
    }
}
