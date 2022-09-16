package dev.racci.minix.api.serializables

import dev.racci.minix.api.extensions.inWholeTicks
import dev.racci.minix.api.extensions.ticks
import dev.racci.minix.api.utils.safeCast
import dev.racci.minix.api.utils.unsafeCast
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import org.bukkit.NamespacedKey
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type
import java.time.Duration

object PotionEffectSerializer : KSerializer<PotionEffect> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PotionEffect") {
        element<String>("type")
        element<Duration>("duration")
        element<Int>("amplifier")
        element<Boolean>("ambient")
        element<Boolean>("has-particles")
        element<Boolean>("has-icon")
        element<NamespacedKey>("namespacedKey")
    }

    override fun serialize(
        encoder: Encoder,
        value: PotionEffect
    ) = encoder.encodeStructure(descriptor) {
        encodeStringElement(descriptor, 0, value.type.name)
        encodeSerializableElement(descriptor, 1, DurationSerializer, value.duration.ticks)
        encodeIntElement(descriptor, 2, value.amplifier)
        encodeBooleanElement(descriptor, 3, value.isAmbient)
        encodeBooleanElement(descriptor, 4, value.hasParticles())
        encodeBooleanElement(descriptor, 5, value.hasIcon())
        if (value.hasKey()) {
            encodeSerializableElement(descriptor, 6, NamespacedKeySerializer, value.key!!)
        }
    }

    override fun deserialize(decoder: Decoder): PotionEffect {
        var type = ""
        var duration = 1.ticks
        var amplifier = 1
        var isAmbient = true
        var particles = true
        var icon = true
        var key: NamespacedKey? = null
        decoder.decodeStructure(descriptor) {
            while (true) {
                when (val i = decodeElementIndex(descriptor)) {
                    0 -> type = decodeStringElement(descriptor, i)
                    1 -> duration = decodeSerializableElement(descriptor, i, DurationSerializer)
                    2 -> amplifier = decodeIntElement(descriptor, i)
                    3 -> isAmbient = decodeBooleanElement(descriptor, i)
                    4 -> particles = decodeBooleanElement(descriptor, i)
                    5 -> icon = decodeBooleanElement(descriptor, i)
                    6 -> key = decodeSerializableElement(
                        NamespacedKeySerializer.descriptor,
                        i,
                        NamespacedKeySerializer
                    )
                    CompositeDecoder.DECODE_DONE -> break
                }
            }
        }
        return PotionEffect(
            PotionEffectType.getByName(type) ?: error("$type is not a valid potion effect type"),
            duration.inWholeTicks.toInt(),
            amplifier,
            isAmbient,
            particles,
            icon,
            key
        )
    }

    object Configurate : TypeSerializer<PotionEffect> {

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
        ): PotionEffect = node.get<Map<String, Any>>()?.let {
            PotionEffect(
                PotionEffectType.getByName(it["type"].unsafeCast()) ?: throw SerializationException("Invalid \"type\" while deserializing: ${it["type"]}."),
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
