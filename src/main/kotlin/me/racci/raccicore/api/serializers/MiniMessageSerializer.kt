@file:Suppress("unused")
package me.racci.raccicore.api.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import me.racci.raccicore.api.extensions.miniMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object MiniMessageSerializer: KSerializer<Component> {

    override val descriptor = PrimitiveSerialDescriptor("MiniMessage", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Component
    ) = encoder.encodeString(MiniMessage.get().serializeOrNull(value)!!)

    override fun deserialize(
        decoder: Decoder
    ) = miniMessage(decoder.decodeString())

}