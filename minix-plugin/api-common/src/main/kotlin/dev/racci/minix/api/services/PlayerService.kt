package dev.racci.minix.api.services

import dev.racci.minix.api.callbacks.PlayerMoveCallback
import dev.racci.minix.api.callbacks.PlayerQuitCallback
import dev.racci.minix.api.collections.player.OnlinePlayerMap
import dev.racci.minix.api.data.PlatformPlayerData
import dev.racci.minix.api.utils.getKoin
import dev.racci.minix.api.utils.minecraft.PlayerUtils
import org.apiguardian.api.API
import org.bukkit.entity.Player
import java.util.UUID

// TODO -> Possible merge into more useful utility class
@API(status = API.Status.MAINTAINED, since = "1.0.0")
public interface PlayerService {

    public val inputCallbacks: OnlinePlayerMap<PlayerUtils.ChatInput>
    public val quitCallbacks: OnlinePlayerMap<PlayerQuitCallback>
    public val moveCallbacks: OnlinePlayerMap<PlayerMoveCallback>

    public operator fun get(player: Player): PlatformPlayerData

    public fun remove(uuid: UUID): Boolean

    public operator fun minusAssign(uuid: UUID)

    public companion object : PlayerService by getKoin().get()
}
