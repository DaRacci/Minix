package me.racci.raccicore.data

import me.racci.raccicore.RacciCore
import me.racci.raccicore.interfaces.IManager
import me.racci.raccicore.utils.extensions.onlinePlayers
import me.racci.raccicore.utils.listen
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID

internal object PlayerManager: IManager<PlayerManager> {
    private val playerData = HashMap<UUID, PlayerData>()
    private var initialized = false

    operator fun get(uuid: UUID) =
        playerData[uuid]!!

    override fun init() {
        val plugin = RacciCore.instance
        if(!initialized) {
            initialized = true
            onlinePlayers.forEach{PlayerData(it).init()}
            plugin.listen<PlayerJoinEvent> {
                if(initialized) PlayerData(it.player).init()
            }
            plugin.listen<PlayerQuitEvent> {
                if(initialized) removePlayerData(it.player.uniqueId)
            }
        } else {
            plugin.log.error("PlayerManager has already been initialized")
        }
    }

    override fun close() {
        playerData.clear()
    }

    fun addPlayerData(playerData: PlayerData) =
        this.playerData.putIfAbsent(playerData.player.uniqueId, playerData)

    fun removePlayerData(id: UUID) {
        playerData.remove(id)
    }

}