package me.racci.raccicore.data

import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

internal class PlayerManager {

    private val playerData: ConcurrentHashMap<UUID, PlayerData> = ConcurrentHashMap()

    fun getPlayerData(id: UUID): PlayerData {
        return playerData[id]!!
    }

    fun addPlayerData(playerData: PlayerData) {
        this.playerData[playerData.player.uniqueId] = playerData
    }

    fun removePlayerData(id: UUID) {
        playerData.remove(id)
    }

    val playerDataMap: ConcurrentMap<UUID, PlayerData>
        get() = playerData

}