package dev.racci.minix.api.serializables

import org.spongepowered.configurate.BasicConfigurationNode
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object PairSerializer : TypeSerializer<Pair<*, *>> {

    override fun deserialize(
        type: Type,
        node: ConfigurationNode,
    ): Pair<*, *> {
        val param = type as ParameterizedType
        val key = param.actualTypeArguments[0]
        val value = param.actualTypeArguments[1]
        val keyMapper = node.options().serializers().get(key) as? TypeSerializer<Any> ?: throw SerializationException("No serializer for key type $key")
        val valueMapper = node.options().serializers().get(value) as? TypeSerializer<Any> ?: throw SerializationException("No serializer for value type $value")
        val keyNode = BasicConfigurationNode.root(node.options())

        val first = keyMapper.deserialize(key, keyNode.node("key")?.get())
        val second = valueMapper.deserialize(value, node.node("value")?.get())
        return first to second
    }

    override fun serialize(
        type: Type,
        obj: Pair<*, *>?,
        node: ConfigurationNode,
    ) {
        if (obj == null) {
            node.raw(null)
            return
        }
        val param = type as ParameterizedType
        val key = param.actualTypeArguments[0]
        val value = param.actualTypeArguments[1]
        val keyMapper = node.options().serializers().get(key) as? TypeSerializer<Any> ?: throw SerializationException("No serializer for key type $key")
        val valueMapper = node.options().serializers().get(value) as? TypeSerializer<Any> ?: throw SerializationException("No serializer for value type $value")

        keyMapper.serialize(key, obj.first, node.node("key"))
        valueMapper.serialize(value, obj.second, node.node("value"))
    }
}
