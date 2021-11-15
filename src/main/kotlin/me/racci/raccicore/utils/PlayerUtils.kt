package me.racci.raccicore.utils

import me.racci.raccicore.RacciPlugin
import me.racci.raccicore.core.Provider
import net.kyori.adventure.text.ComponentLike
import org.bukkit.entity.Player
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


typealias ChatInputCallBack = Player.(ComponentLike) -> Unit
typealias PlayerCallbackFunction<R> = Player.() -> R
typealias PlayerQuitFunction = PlayerCallbackFunction<Unit>
typealias PlayerMoveFunction = PlayerCallbackFunction<Boolean>

object PlayerUtils {

    fun Player.chatInput(
        plugin: RacciPlugin,
        whenQuitWithoutInput: PlayerQuitFunction = {},
        callback: ChatInputCallBack
    ) {
        Provider.playerManager.inputCallbacks.put(
            player!!,
            ChatInput(plugin, callback, whenQuitWithoutInput)
        ) { it.playerQuit(this) }
    }

    // null if player disconnect
    suspend fun Player.chatInput(
        plugin: RacciPlugin
    ): ComponentLike? = suspendCoroutine { c ->
        chatInput(plugin, { c.resume(null) }) { c.resume(it) }
    }

    fun Player.whenQuit(
        plugin: RacciPlugin,
        callback: PlayerQuitFunction
    ) {
        Provider.playerManager.functionsQuit.put(this, PlayerCallback(plugin, callback)) {
            it.callback.invoke(player!!)
        }
    }

    fun Player.whenMove(
        plugin: RacciPlugin,
        callback: PlayerMoveFunction
    ) {
        Provider.playerManager.functionsMove[this] = PlayerCallback(plugin, callback)
    }

    class ChatInput(
        val plugin: RacciPlugin,
        val callback: ChatInputCallBack,
        val playerQuit: PlayerQuitFunction
    )
    class PlayerCallback<R>(
        val plugin: RacciPlugin,
        val callback: PlayerCallbackFunction<R>
    )



}