@file:Suppress("unused")

package dev.racci.minix.api.serializables

import dev.racci.minix.api.extensions.parse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage.miniMessage

object MiniMessageSerializer : KSerializer<Component> {

    override val descriptor = PrimitiveSerialDescriptor("MiniMessage", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Component,
    ) = encoder.encodeString(miniMessage().serializeOrNull(value)!!)

    override fun deserialize(
        decoder: Decoder,
    ): Component = miniMessage().parse(decoder.decodeString())
}
