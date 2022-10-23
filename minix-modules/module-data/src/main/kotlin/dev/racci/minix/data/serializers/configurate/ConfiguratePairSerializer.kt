package dev.racci.minix.data.serializers.configurate

import dev.racci.minix.api.extensions.reflection.safeCast
import org.spongepowered.configurate.BasicConfigurationNode
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

public object ConfiguratePairSerializer : TypeSerializer<Pair<*, *>> {

    override fun deserialize(
        type: Type,
        node: ConfigurationNode
    ): Pair<*, *> {
        val (key, value) = getTypes(type)
        val (keyMapper, valueMapper) = getMappers(node, key, value)
        val keyNode = BasicConfigurationNode.root(node.options())

        val first = keyMapper.deserialize(key, keyNode.node("key")?.get())
        val second = valueMapper.deserialize(value, node.node("value")?.get())
        return first to second
    }

    override fun serialize(
        type: Type,
        obj: Pair<*, *>?,
        node: ConfigurationNode
    ) {
        if (obj == null) {
            node.raw(null)
            return
        }
        val (key, value) = getTypes(type)
        val (keyMapper, valueMapper) = getMappers(node, key, value)

        keyMapper.serialize(key, obj.first, node.node("key"))
        valueMapper.serialize(value, obj.second, node.node("value"))
    }

    private fun getTypes(type: Type): Pair<Type, Type> {
        val param = type as ParameterizedType
        val key = param.actualTypeArguments[0]
        val value = param.actualTypeArguments[1]

        return key to value
    }

    private fun getMappers(
        node: ConfigurationNode,
        key: Type,
        value: Type
    ): Pair<TypeSerializer<Any>, TypeSerializer<Any>> {
        val keyMapper = node.options().serializers().get(key).safeCast<TypeSerializer<Any>>() ?: throw SerializationException("No serializer for key type $key")
        val valueMapper = node.options().serializers().get(value).safeCast<TypeSerializer<Any>>() ?: throw SerializationException("No serializer for value type $value")

        return keyMapper to valueMapper
    }
}
