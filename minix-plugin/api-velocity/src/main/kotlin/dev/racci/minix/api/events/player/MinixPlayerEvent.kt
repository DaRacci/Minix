package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.MinixPlayer

public actual abstract class MinixPlayerEvent {
    actual val minixPlayer: MinixPlayer
        get() = TODO("Not yet implemented")
    actual val async: Boolean
        get() = TODO("Not yet implemented")
    actual var cancelled: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}

    actual fun toString(): String {
        TODO("Not yet implemented")
    }

    actual fun equals(other: Any?): Boolean {
        TODO("Not yet implemented")
    }

    actual fun hashCode(): Int {
        TODO("Not yet implemented")
    }

}