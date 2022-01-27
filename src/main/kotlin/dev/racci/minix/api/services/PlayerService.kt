package dev.racci.minix.api.services

import dev.racci.minix.api.utils.collections.OnlinePlayerMap
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.minecraft.PlayerUtils
import dev.racci.minix.core.data.PlayerData
import java.util.UUID

interface PlayerService {

    val playerData: HashMap<UUID, PlayerData>
    val inputCallbacks: OnlinePlayerMap<PlayerUtils.ChatInput>
    val functionsQuit: OnlinePlayerMap<PlayerUtils.PlayerCallback<Unit>>
    val functionsMove: OnlinePlayerMap<PlayerUtils.PlayerCallback<Boolean>>

    fun remove(uuid: UUID): Boolean
    operator fun get(uuid: UUID): PlayerData
    operator fun minusAssign(uuid: UUID)

    companion object : PlayerService by getKoin().get()
}
