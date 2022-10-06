package dev.racci.minix.api.services

import dev.racci.minix.api.data.PlayerData
import dev.racci.minix.api.utils.collections.OnlinePlayerMap
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.minecraft.PlayerUtils
import org.apiguardian.api.API
import org.bukkit.entity.Player
import java.util.UUID

@API(status = API.Status.MAINTAINED, since = "1.0.0")
interface PlayerService {

    val inputCallbacks: OnlinePlayerMap<PlayerUtils.ChatInput>
    val functionsQuit: OnlinePlayerMap<PlayerUtils.PlayerCallback<Unit>>
    val functionsMove: OnlinePlayerMap<PlayerUtils.PlayerCallback<Boolean>>

    operator fun get(player: Player): PlayerData

    fun remove(uuid: UUID): Boolean

    operator fun minusAssign(uuid: UUID)

    companion object : PlayerService by getKoin().get()
}
