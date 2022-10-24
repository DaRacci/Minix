package dev.racci.minix.api.data

import dev.racci.minix.api.exceptions.WrappingException
import net.kyori.adventure.text.Component
import java.util.UUID

public expect class MinixPlayer : PlatformPlayerData {

    public val uuid: UUID

    public val name: String

    public val displayName: Component

    public val isOnline: Boolean

    public companion object {
        /**
         * Wraps a player object from the platform into a MinixPlayer.
         * @param obj The player object to wrap.
         * @return The wrapped player.
         * @throws WrappingException If the player object is not the platforms' player type.
         */
        @Throws(WrappingException::class)
        public fun wrapped(obj: Any): MinixPlayer
    }
}
