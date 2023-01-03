package dev.racci.minix.data.serializers.kotlin.minecraft

import dev.racci.minix.api.utils.minecraft.MCVersion
import dev.racci.minix.api.utils.ticks
import dev.racci.minix.data.extensions.bukkit
import dev.racci.minix.data.extensions.encodeNonDefault
import dev.racci.minix.data.extensions.encodeNonNull
import dev.racci.minix.data.extensions.supportsPurpur
import dev.racci.minix.data.serializers.kotlin.DurationSerializer
import dev.racci.minix.data.serializers.kotlin.KeySerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.serializer
import org.bukkit.NamespacedKey
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.time.Duration

public object PotionEffectSerializer : KSerializer<PotionEffect> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PotionEffect") {
        element<PotionEffectType>("type")
        element<Duration>("duration")
        element<Int>("amplifier", isOptional = true)
        element<Boolean>("ambient", isOptional = true)
        element<Boolean>("has-particles", isOptional = true)
        element<Boolean>("has-icon", isOptional = true)
        element<NamespacedKey>("namespacedKey", isOptional = true)
    }

    override fun serialize(
        encoder: Encoder,
        value: PotionEffect
    ): Unit = encoder.encodeStructure(descriptor) {
        encodeSerializableElement(descriptor, 0, serializer(), value.type)
        encodeSerializableElement(descriptor, 1, DurationSerializer, value.duration.ticks)
        encodeIntElement(descriptor, 2, value.amplifier)
        encodeNonDefault(descriptor, 3, value.isAmbient, true)
        encodeNonDefault(descriptor, 4, value.hasParticles(), true)
        encodeNonDefault(descriptor, 5, value.hasIcon(), true)
        if (MCVersion.supportsPurpur) encodeNonNull(descriptor, 6, KeySerializer, value.key)
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): PotionEffect = decoder.decodeStructure(descriptor) {
        val type = decodeSerializableElement(descriptor, 0, serializer<PotionEffectType>())
        val duration = decodeSerializableElement(descriptor, 1, DurationSerializer).ticks.toInt()
        val amplifier = decodeIntElement(descriptor, 2)
        val ambient = decodeNullableSerializableElement(descriptor, 3, serializer()) ?: true
        val particles = decodeNullableSerializableElement(descriptor, 5, serializer()) ?: ambient
        val icon = decodeNullableSerializableElement(descriptor, 6, serializer()) ?: ambient
        val key = decodeNullableSerializableElement(descriptor, 7, KeySerializer.nullable)

        PotionEffect(
            type,
            duration,
            amplifier,
            ambient,
            particles,
            icon,
            key?.bukkit
        )
    }
}
