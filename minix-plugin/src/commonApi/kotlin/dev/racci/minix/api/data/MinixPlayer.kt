package dev.racci.minix.api.data

import dev.racci.minix.api.wrappers.WrapperCompanion
import net.kyori.adventure.text.Component
import java.util.UUID

/**
 * A Wrapper for the platforms player object.
 * This wrapper also provides a few useful functions.
 */
public expect class MinixPlayer : PlatformPlayerData {

    public val uuid: UUID

    public val name: String

    public val displayName: Component

    public val isOnline: Boolean

    public companion object : WrapperCompanion<MinixPlayer> {
        internal val EMPTY: MinixPlayer

        override fun wrapped(obj: Any): MinixPlayer

        // For the Player service
        internal fun of(obj: Any): MinixPlayer
    }
}
