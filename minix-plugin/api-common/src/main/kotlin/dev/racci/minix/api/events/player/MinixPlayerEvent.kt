package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.MinixPlayer

public expect abstract class MinixPlayerEvent {
    public val minixPlayer: MinixPlayer

    public val async: Boolean

    override fun toString(): String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}
