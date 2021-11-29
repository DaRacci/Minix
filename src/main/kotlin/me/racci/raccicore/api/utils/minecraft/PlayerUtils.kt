@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")
package me.racci.raccicore.api.utils.minecraft

import me.racci.raccicore.api.plugin.RacciPlugin
import me.racci.raccicore.core.managers.PlayerManager
import net.kyori.adventure.text.ComponentLike
import org.bukkit.entity.Player
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Utilities for Players
 */
object PlayerUtils {

    fun Player.chatInput(
        plugin: RacciPlugin,
        whenQuitWithoutInput: PlayerQuitFunction = {},
        callback: ChatInputCallBack
    ) {
        PlayerManager.inputCallbacks.put(
            player!!,
            ChatInput(plugin, callback, whenQuitWithoutInput)
        ) {it.playerQuit(this)}
    }

    // null if player disconnect
    suspend fun Player.chatInput(
        plugin: RacciPlugin
    ): ComponentLike? = suspendCoroutine {c->
        chatInput(plugin, {c.resume(null)}) {c.resume(it)}
    }

    fun Player.whenQuit(
        plugin: RacciPlugin,
        callback: PlayerQuitFunction
    ) {
        PlayerManager.functionsQuit.put(this, PlayerCallback(plugin, callback)) {
            it.callback.invoke(player!!)
        }
    }

    fun Player.whenMove(
        plugin: RacciPlugin,
        callback: PlayerMoveFunction
    ) {
        PlayerManager.functionsMove[this] = PlayerCallback(plugin, callback)
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