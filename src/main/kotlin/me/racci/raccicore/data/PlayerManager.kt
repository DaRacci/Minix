package me.racci.raccicore.data

import me.racci.raccicore.RacciCore
import me.racci.raccicore.utils.extensions.onlinePlayers
import me.racci.raccicore.utils.listen
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID

internal object PlayerManager {
    private val playerData = HashMap<UUID, PlayerData>()

    operator fun get(uuid: UUID) =
        playerData[uuid]!!

    fun init(plugin: RacciCore) {
        onlinePlayers.forEach{PlayerData(it).init()}
        plugin.listen<PlayerJoinEvent> {
            PlayerData(it.player).init()
        }
        plugin.listen<PlayerQuitEvent> {
            removePlayerData(it.player.uniqueId)
        }
    }

    fun close() {
        playerData.clear()
    }

    fun addPlayerData(playerData: PlayerData) =
        this.playerData.putIfAbsent(playerData.player.uniqueId, playerData)

    fun removePlayerData(id: UUID) {
        playerData.remove(id)
    }

}