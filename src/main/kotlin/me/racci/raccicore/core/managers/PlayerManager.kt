@file:Suppress("UNUSED")
package me.racci.raccicore.core.managers

import io.papermc.paper.event.player.AsyncChatEvent
import me.racci.raccicore.api.events.PlayerUnloadEvent
import me.racci.raccicore.api.extensions.KListener
import me.racci.raccicore.api.extensions.displaced
import me.racci.raccicore.api.extensions.event
import me.racci.raccicore.api.lifecycle.LifecycleListener
import me.racci.raccicore.api.utils.collections.onlinePlayerMapOf
import me.racci.raccicore.api.utils.minecraft.PlayerUtils
import me.racci.raccicore.core.RacciCore
import me.racci.raccicore.core.data.PlayerData
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.PluginDisableEvent
import java.util.*

class PlayerManager(
    override val plugin: RacciCore
): LifecycleListener<RacciCore>, KListener<RacciCore> {

    private val playerData = HashMap<UUID, PlayerData>()

    val inputCallbacks by lazy {onlinePlayerMapOf<PlayerUtils.ChatInput>()}
    val functionsQuit  by lazy {onlinePlayerMapOf<PlayerUtils.PlayerCallback<Unit>>()}
    val functionsMove  by lazy {onlinePlayerMapOf<PlayerUtils.PlayerCallback<Boolean>>()}

    operator fun get(
        uuid: UUID
    ) = playerData.computeIfAbsent(uuid) {PlayerData(uuid).also{it.inAccess++}}

    override suspend fun onEnable() {
        event<PlayerQuitEvent> {
            playerData[player.uniqueId]?.inAccess?.minus(1)
        }
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
        event<PlayerUnloadEvent> {
            HookManager.floodgateCache.remove(uuid)
        }
    }

    override suspend fun onDisable() {
        playerData.clear()
    }

    companion object {
        private lateinit var INSTANCE: PlayerManager

        operator fun get(uuid: UUID) = INSTANCE[uuid]
        internal fun remove(uuid: UUID) = INSTANCE.playerData.remove(uuid)
        val inputCallbacks get() = INSTANCE.inputCallbacks
        val functionsQuit  get() = INSTANCE.functionsQuit
        val functionsMove  get() = INSTANCE.functionsMove
    }

}