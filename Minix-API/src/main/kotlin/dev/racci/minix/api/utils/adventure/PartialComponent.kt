package dev.racci.minix.api.utils.adventure

import dev.racci.minix.api.extensions.lazyPlaceholder
import dev.racci.minix.api.extensions.msg
import dev.racci.minix.api.plugin.logger.MinixLogger
import dev.racci.minix.api.utils.getKoin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

class PartialComponent private constructor(private var raw: String) {
    private var _value = raw
    private var dirty = true
    private var cache: Component? = null

    val value: Component
        get() {
            if (dirty) {
                cache = MiniMessage.miniMessage().deserialize(_value)
                dirty = false
            }
            return cache!!
        }

    operator fun get(vararg placeholder: Pair<String, () -> Any>): Component = if (placeholder.isEmpty()) {
        value
    } else MiniMessage.miniMessage().lazyPlaceholder(_value, placeholder)

    fun formatRaw(placeholders: Map<String, String>) {
        var tmp = raw

        for ((placeholder, prefix) in placeholders.entries) {
            val index = tmp.indexOf(placeholder).takeIf { it != -1 } ?: continue
            tmp = tmp.replaceRange(index, index + placeholder.length, prefix)

            dirty = true
            cache = null
            getKoin().get<MinixLogger>().debug { "Replaced $placeholder in $raw" }
        }

        _value = tmp
    }

    companion object {

        fun of(raw: String): PartialComponent {
            return PartialComponent(raw)
        }

        infix fun PartialComponent.message(recipient: CommandSender) = recipient.msg(this.get())
    }

    object Serializer : TypeSerializer<PartialComponent> {

        override fun deserialize(
            type: Type,
            node: ConfigurationNode
        ): PartialComponent = node.get<String>()?.let(PartialComponent::of) ?: throw SerializationException(type, "Null Partial Component: ${node.path()}")

        override fun serialize(
            type: Type,
            obj: PartialComponent?,
            node: ConfigurationNode
        ) {
            if (obj == null) { node.raw(null); return }
            node.set(obj.raw)
        }
    }
}
