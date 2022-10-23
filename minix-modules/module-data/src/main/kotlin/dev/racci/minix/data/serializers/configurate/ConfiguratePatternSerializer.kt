package dev.racci.minix.data.serializers.configurate

import org.bukkit.DyeColor
import org.bukkit.block.banner.Pattern
import org.bukkit.block.banner.PatternType
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

public object ConfiguratePatternSerializer : TypeSerializer<Pattern> {

    override fun serialize(
        type: Type,
        obj: Pattern?,
        node: ConfigurationNode
    ) {
        if (obj == null) {
            node.raw(null); return
        }
        node.set("${obj.color.name}:${obj.pattern}")
    }

    override fun deserialize(
        type: Type,
        node: ConfigurationNode
    ): Pattern = node.get<String>()?.split(':', limit = 2)?.let { Pattern(DyeColor.valueOf(it[0]), PatternType.valueOf(it[1])) }
        ?: error("Invalid pattern, expected [colour:pattern]")
}
