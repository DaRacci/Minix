package me.racci.raccilib.utils.text

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer


class Legacy private constructor() {
    companion object {

        val SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build()
    }

    init {
        throw UnsupportedOperationException("Class should not be instantiated!")
    }
}