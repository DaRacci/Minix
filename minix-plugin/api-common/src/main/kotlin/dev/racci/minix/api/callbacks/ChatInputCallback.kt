package dev.racci.minix.api.callbacks

import dev.racci.minix.api.data.MinixPlayer
import net.kyori.adventure.text.ComponentLike
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
public fun interface ChatInputCallback {
    public fun invoke(input: ComponentLike)

    public companion object {
        public val empty: ChatInputCallback = ChatInputCallback { }
    }
}

@ApiStatus.Experimental
public fun interface PlayerQuitCallback {
    public operator fun invoke(player: MinixPlayer)
    public companion object {
        public val empty: PlayerQuitCallback = PlayerQuitCallback { }
    }
}

@ApiStatus.Experimental
public fun interface PlayerMoveCallback {
    public fun invoke(player: MinixPlayer): Boolean
    public companion object {
        public val empty: PlayerMoveCallback = PlayerMoveCallback { true }
    }
}

@ApiStatus.Experimental
public fun interface PlayerQuitMapCallback<V> {
    public fun invoke(player: MinixPlayer, value: V)
    public companion object {
        public val empty: PlayerQuitMapCallback<Nothing> = PlayerQuitMapCallback { _, _ -> }
    }
}
