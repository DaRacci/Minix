package dev.racci.minix.api.events.player

import dev.racci.minix.api.data.MinixPlayer
import org.bukkit.entity.Player

public expect abstract class MinixPlayerEvent {
    public val minixPlayer: MinixPlayer

    public val async: Boolean

    public operator fun component1(): Player

    override fun toString(): String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}
