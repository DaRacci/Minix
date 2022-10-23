package dev.racci.minix.data.serializers.configurate

import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.extensions.reflection.safeCast
import org.bukkit.NamespacedKey
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

public object ConfiguratePotionSerializer : TypeSerializer<PotionEffect> {

    override fun serialize(
        type: Type,
        obj: PotionEffect?,
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
    ): PotionEffect {
        val mapNode = node.get<Map<String, Any>>() ?: throw SerializationException("Invalid potion effect node")
        PotionEffect(
            PotionEffectType.getByKey(mapNode["type"].castOrThrow()) ?: throw SerializationException("Invalid type while deserializing: ${mapNode["type"]}."),
            mapNode["duration"].castOrThrow(),
            mapNode["amplifier"].castOrThrow(),
            mapNode["ambient"].castOrThrow(),
            mapNode["has-particles"].castOrThrow(),
            mapNode["hasIcon"].castOrThrow(),
            mapNode["key"].castOrThrow()
        )

        return node.get<Map<String, Any>>()?.let {
            PotionEffect(
                PotionEffectType.getByName(it["type"].castOrThrow()) ?: throw SerializationException("Invalid \"type\" while deserializing: ${it["type"]}."),
                it["duration"].safeCast() ?: it["duration"].toString().toIntOrNull()
                    ?: throw SerializationException("Invalid \"duration\" while deserializing: ${it["duration"]}"),
                it["amplifier"].safeCast() ?: it["amplifier"].toString().toIntOrNull()
                    ?: throw SerializationException("Invalid \"amplifier\" while deserializing: ${it["amplifier"]}"),
                it["ambient"].safeCast() ?: it["ambient"].toString().toBooleanStrictOrNull()
                    ?: throw SerializationException("Invalid \"ambient\" while deserializing: ${it["ambient"]}"),
                it["has-particles"].safeCast() ?: it["has-particles"].toString().toBooleanStrictOrNull() ?: throw SerializationException(
                    type,
                    "Invalid \"has-particles\" while deserializing: ${it["hasParticles"]}"
                ),
                it["hasIcon"].safeCast() ?: it["hasIcon"].toString().toBooleanStrictOrNull() ?: throw SerializationException(
                    type,
                    "Invalid \"hasIcon\" while deserializing: ${it["hasIcon"]}"
                ),
                it["key"]?.let { str -> NamespacedKey.fromString(str.toString()) }
            )
        } ?: error("Cannot deserialize null")
    }
}
