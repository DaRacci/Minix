package dev.racci.minix.api.utils.minecraft

import dev.racci.minix.api.callbacks.ChatInputCallback
import dev.racci.minix.api.callbacks.PlayerMoveCallback
import dev.racci.minix.api.callbacks.PlayerQuitCallback
import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.services.PlayerService
import net.kyori.adventure.text.ComponentLike
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Utilities for Players.
 */
public object PlayerUtils {

    public fun MinixPlayer.chatInput(
        whenQuitWithoutInput: PlayerQuitCallback = PlayerQuitCallback.empty,
        callback: ChatInputCallback
    ) {
        PlayerService.inputCallbacks.put(
            player!!,
            ChatInput(plugin, callback, whenQuitWithoutInput)
        ) { it.playerQuit(this) }
    }

    // null if player disconnect
    public suspend fun MinixPlayer.chatInput(): ComponentLike? = suspendCoroutine { c ->
        chatInput(plugin, { c.resume(null) }) { c.resume(it) }
    }

    public fun MinixPlayer.whenQuit(callback: PlayerQuitCallback) {
        PlayerService.functionsQuit.put(this, PlayerCallback(plugin, callback)) {
            it.callback.invoke(player!!)
        }
    }

    public fun MinixPlayer.whenMove(callback: PlayerMoveCallback) {
        PlayerService.moveCallbacks[this] = callback
    }

    public data class ChatInput(
        val callback: ChatInputCallback,
        val playerQuit: PlayerQuitCallback
    )
}
