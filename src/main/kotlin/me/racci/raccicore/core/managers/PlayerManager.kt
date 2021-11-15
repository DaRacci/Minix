package me.racci.raccicore.core.managers

import io.papermc.paper.event.player.AsyncChatEvent
import me.racci.raccicore.core.RacciCore
import me.racci.raccicore.core.data.PlayerManager
import me.racci.raccicore.extensions.KListener
import me.racci.raccicore.extensions.displaced
import me.racci.raccicore.extensions.event
import me.racci.raccicore.lifecycle.LifecycleListener
import me.racci.raccicore.utils.PlayerUtils
import me.racci.raccicore.utils.collections.onlinePlayerMapOf
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.server.PluginDisableEvent

class PlayerManager(
    override val plugin: RacciCore,
): KListener<RacciCore>, LifecycleListener<RacciCore> {

    val inputCallbacks by lazy {onlinePlayerMapOf<PlayerUtils.ChatInput>()}
    val functionsQuit  by lazy {onlinePlayerMapOf<PlayerUtils.PlayerCallback<Unit>>()}
    val functionsMove  by lazy {onlinePlayerMapOf<PlayerUtils.PlayerCallback<Boolean>>()}

    override suspend fun onEnable() {
        PlayerManager.init()
        event<AsyncChatEvent>(ignoreCancelled = true) {
            val input = inputCallbacks.remove(player)
            input?.callback?.invoke(player, message())
            isCancelled = true
        }
        event<PlayerMoveEvent>(ignoreCancelled = true) {
            if(displaced && functionsMove[player]?.run {callback.invoke(player)} == true) {
                isCancelled = true
            }
        }
        event<PluginDisableEvent> {
            inputCallbacks.entries.filter { it.value.plugin == plugin }.forEach {
                inputCallbacks.remove(it.key)
            }
            functionsMove.entries.filter { it.value.plugin == plugin }.forEach {
                functionsMove.remove(it.key)
            }
            functionsQuit.entries.filter { it.value.plugin == plugin }.forEach {
                functionsQuit.remove(it.key)
            }
        }
    }

    override suspend fun onDisable() {
        PlayerManager.close()
    }

}