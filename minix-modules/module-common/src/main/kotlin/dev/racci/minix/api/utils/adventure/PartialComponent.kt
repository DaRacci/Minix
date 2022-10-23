package dev.racci.minix.api.utils.adventure

import dev.racci.minix.api.extensions.lazyPlaceholder
import kotlinx.serialization.Transient
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.SerializationException
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

public class PartialComponent private constructor(private var raw: String) : ComponentLike {
    @Transient private var _value = raw

    @Transient private var dirty = true

    @Transient private var cache: Component? = null

    @Transient public val value: Component
        get() {
            if (dirty) {
                cache = MiniMessage.miniMessage().deserialize(_value)
                dirty = false
            }
            return cache!!
        }

    public operator fun get(vararg placeholder: Pair<String, () -> Any>): Component = if (placeholder.isEmpty()) {
        value
    } else MiniMessage.miniMessage().lazyPlaceholder(_value, placeholder)

    public fun formatRaw(placeholders: Map<String, String>) {
        var tmp = raw

        for ((placeholder, prefix) in placeholders.entries) {
            val index = tmp.indexOf(placeholder).takeIf { it != -1 } ?: continue
            tmp = tmp.replaceRange(index, index + placeholder.length, prefix)

            dirty = true
            cache = null
        }

        _value = tmp
    }

    override fun asComponent(): Component = this.get()

    override fun toString(): String {
        return "PartialComponent(raw='$raw', _value='$_value', dirty=$dirty, cache=$cache)"
    }

    override fun hashCode(): Int {
        var result = raw.hashCode()
        result = 31 * result + _value.hashCode()
        result = 31 * result + dirty.hashCode()
        result = 31 * result + (cache?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PartialComponent) return false

        if (raw != other.raw) return false
        if (_value != other._value) return false
        if (dirty != other.dirty) return false
        if (cache != other.cache) return false

        return true
    }

    public companion object {

        public fun of(raw: String): PartialComponent {
            return PartialComponent(raw)
        }
    }

    public object Serializer : TypeSerializer<PartialComponent> {

        public override fun deserialize(
            type: Type,
            node: ConfigurationNode
        ): PartialComponent = node.get<String>()?.let(Companion::of) ?: throw SerializationException(type, "Null Partial Component: ${node.path()}")

        public override fun serialize(
            type: Type,
            obj: PartialComponent?,
            node: ConfigurationNode
        ) {
            if (obj == null) { node.raw(null); return }
            node.set(obj.raw)
        }
    }
}
