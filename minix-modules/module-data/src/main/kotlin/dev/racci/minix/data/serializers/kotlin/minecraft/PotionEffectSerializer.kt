package dev.racci.minix.data.serializers.kotlin.minecraft

import dev.racci.minix.api.utils.ticks
import dev.racci.minix.data.serializers.kotlin.DurationSerializer
import dev.racci.minix.data.serializers.kotlin.KeySerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.time.Duration

public object PotionEffectSerializer : KSerializer<PotionEffect> {

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
    ): Unit = encoder.encodeStructure(descriptor) {
        encodeStringElement(descriptor, 0, value.type.name)
        encodeSerializableElement(descriptor, 1, DurationSerializer, value.duration.ticks)
        encodeIntElement(descriptor, 2, value.amplifier)
        encodeBooleanElement(descriptor, 3, value.isAmbient)
        encodeBooleanElement(descriptor, 4, value.hasParticles())
        encodeBooleanElement(descriptor, 5, value.hasIcon())
        if (value.hasKey()) {
            encodeSerializableElement(descriptor, 6, KeySerializer, value.key!!)
        }
    }

    override fun deserialize(decoder: Decoder): PotionEffect {
        var type = ""
        var duration = 1.ticks
        var amplifier = 1
        var isAmbient = true
        var particles = true
        var icon = true
        var key: Key? = null
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
                        KeySerializer.descriptor,
                        i,
                        KeySerializer
                    )
                    CompositeDecoder.DECODE_DONE -> break
                }
            }
        }
        return PotionEffect(
            PotionEffectType.getByName(type) ?: error("$type is not a valid potion effect type"),
            (duration.inWholeMilliseconds / 50).toInt(),
            amplifier,
            isAmbient,
            particles,
            icon,
            NamespacedKey.fromString(key.toString())
        )
    }
}
