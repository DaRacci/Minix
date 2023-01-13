package dev.racci.minix.api.utils.minecraft

import dev.racci.minix.api.callbacks.ChatInputCallback
import dev.racci.minix.api.callbacks.PlayerMoveCallback
import dev.racci.minix.api.callbacks.PlayerQuitCallback
import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.services.PlayerService
import net.kyori.adventure.text.ComponentLike
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// TODO -> Rework
/**
 * Utilities for Players.
 */
public object PlayerUtils {

    public fun MinixPlayer.chatInput(
        whenQuitWithoutInput: PlayerQuitCallback = PlayerQuitCallback.empty,
        callback: ChatInputCallback
    ) {
        PlayerService.inputCallbacks.put(
            this,
            ChatInput(callback, whenQuitWithoutInput),
            onQuit = { player, _ -> whenQuitWithoutInput(player) }
        )
    }

    // null if player disconnect
    public suspend fun MinixPlayer.chatInput(): ComponentLike? = suspendCoroutine { c ->
        chatInput({ c.resume(null) }) { c.resume(it) }
    }

    public fun MinixPlayer.whenQuit(callback: PlayerQuitCallback) {
        PlayerService.quitCallbacks.put(this, callback)
    }

    public fun MinixPlayer.whenMove(callback: PlayerMoveCallback) {
        PlayerService.moveCallbacks.put(this, callback)
    }

    public data class ChatInput(
        val callback: ChatInputCallback,
        val playerQuit: PlayerQuitCallback
    )
}
