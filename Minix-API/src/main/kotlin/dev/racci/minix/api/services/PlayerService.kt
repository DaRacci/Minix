package dev.racci.minix.api.services

import dev.racci.minix.api.utils.collections.OnlinePlayerMap
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.minecraft.PlayerUtils
import org.apiguardian.api.API
import java.util.UUID

@API(status = API.Status.MAINTAINED, since = "1.0.0")
interface PlayerService {

    val inputCallbacks: OnlinePlayerMap<PlayerUtils.ChatInput>
    val functionsQuit: OnlinePlayerMap<PlayerUtils.PlayerCallback<Unit>>
    val functionsMove: OnlinePlayerMap<PlayerUtils.PlayerCallback<Boolean>>

    fun remove(uuid: UUID): Boolean

    operator fun minusAssign(uuid: UUID)

    companion object : PlayerService by getKoin().get()
}
