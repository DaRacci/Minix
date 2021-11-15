package me.racci.raccicore.managers

import io.papermc.paper.event.player.AsyncChatEvent
import me.racci.raccicore.core.RacciCore
import me.racci.raccicore.extensions.KListener
import me.racci.raccicore.extensions.displaced
import me.racci.raccicore.extensions.event
import me.racci.raccicore.utils.PlayerUtils
import me.racci.raccicore.utils.collections.onlinePlayerMapOf
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.server.PluginDisableEvent
import kotlin.properties.Delegates

internal object PlayerManager: KListener<RacciCore> {
    override var plugin: RacciCore by Delegates.notNull()

    val inputCallbacks by lazy {plugin.onlinePlayerMapOf<PlayerUtils.ChatInput>()}
    val functionsQuit  by lazy {plugin.onlinePlayerMapOf<PlayerUtils.PlayerCallback<Unit>>()}
    val functionsMove  by lazy {plugin.onlinePlayerMapOf<PlayerUtils.PlayerCallback<Boolean>>()}

    fun init(plugin: RacciCore) {
        this.plugin = plugin
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


}