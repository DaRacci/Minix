package me.racci.raccilib.utils.text

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer


class Legacy private constructor() {
    companion object {

        val SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build()
    }
}
object LegacyUtils {
    fun parseLegacy(string: String?): Component? {
        return LegacyComponentSerializer.legacySection().deserialize(string!!)
    }
}