package dev.racci.minix.api.utils.primitive

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.jetbrains.annotations.ApiStatus

/**
 * Legacy
 *
 * @constructor Create empty Legacy
 */
@Deprecated("MiniMessage will be the only supported text format going forward", ReplaceWith("MiniMessage.miniMessage().deserialize(string)"))
@ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
class Legacy private constructor() {

    companion object {

        val SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build()
    }
}

@Deprecated("MiniMessage will be the only supported text format going forward", ReplaceWith("MiniMessage.miniMessage().deserialize(string)"))
@ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
object LegacyUtils {

    @Deprecated("MiniMessage will be the only supported text format going forward", ReplaceWith("MiniMessage.miniMessage().deserialize(string)"))
    @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
    fun parseLegacy(string: String): Component {
        return LegacyComponentSerializer.legacySection().deserialize(string)
    }

    @Deprecated("MiniMessage will be the only supported text format going forward", ReplaceWith("MiniMessage.miniMessage().deserialize(string)"))
    @ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
    fun parseLegacy(list: List<String>?): List<Component> {
        val compList = ArrayList<Component>()
        list?.forEach { var1x -> compList.add(LegacyComponentSerializer.legacySection().deserialize(var1x)) }
        return compList
    }
}
