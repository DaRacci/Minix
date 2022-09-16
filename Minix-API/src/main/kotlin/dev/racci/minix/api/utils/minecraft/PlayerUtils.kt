package dev.racci.minix.api.utils.minecraft

import dev.racci.minix.api.aliases.ChatInputCallBack
import dev.racci.minix.api.aliases.PlayerCallbackFunction
import dev.racci.minix.api.aliases.PlayerMoveFunction
import dev.racci.minix.api.aliases.PlayerQuitFunction
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.services.PlayerService
import net.kyori.adventure.text.ComponentLike
import org.bukkit.entity.Player
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Utilities for Players.
 */
object PlayerUtils {

    fun Player.chatInput(
        plugin: MinixPlugin,
        whenQuitWithoutInput: PlayerQuitFunction = {},
        callback: ChatInputCallBack
    ) {
        PlayerService.inputCallbacks.put(
            player!!,
            ChatInput(plugin, callback, whenQuitWithoutInput)
        ) { it.playerQuit(this) }
    }

    // null if player disconnect
    suspend fun Player.chatInput(
        plugin: MinixPlugin
    ): ComponentLike? = suspendCoroutine { c ->
        chatInput(plugin, { c.resume(null) }) { c.resume(it) }
    }

    fun Player.whenQuit(
        plugin: MinixPlugin,
        callback: PlayerQuitFunction
    ) {
        PlayerService.functionsQuit.put(this, PlayerCallback(plugin, callback)) {
            it.callback.invoke(player!!)
        }
    }

    fun Player.whenMove(
        plugin: MinixPlugin,
        callback: PlayerMoveFunction
    ) {
        PlayerService.functionsMove[this] = PlayerCallback(plugin, callback)
    }

    data class ChatInput(
        val plugin: MinixPlugin,
        val callback: ChatInputCallBack,
        val playerQuit: PlayerQuitFunction
    )

    data class PlayerCallback<R>(
        val plugin: MinixPlugin,
        val callback: PlayerCallbackFunction<R>
    )
}
