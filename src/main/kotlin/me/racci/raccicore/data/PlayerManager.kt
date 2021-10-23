package me.racci.raccicore.data

import me.racci.raccicore.utils.extensions.onlinePlayers
import java.util.*
import java.util.concurrent.ConcurrentHashMap

internal object PlayerManager {
    private val playerData = ConcurrentHashMap<UUID, PlayerData>()

    operator fun get(uuid: UUID) =
        playerData[uuid]!!

    fun init() {

        onlinePlayers.forEach(::PlayerData)

    }

    fun shutdown() {
        playerData.clear()
    }

    @Deprecated("Use operator method instead")
    fun getPlayerData(id: UUID) = playerData[id]!!

    fun addPlayerData(playerData: PlayerData) =
        this.playerData.putIfAbsent(playerData.player.uniqueId, playerData)

    fun removePlayerData(id: UUID) {
        playerData.remove(id)
    }

}