@file:Suppress("UNUSED")

package dev.racci.minix.core.services

import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extensions.cancel
import dev.racci.minix.api.extensions.displaced
import dev.racci.minix.api.extensions.event
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.services.PlayerService
import dev.racci.minix.api.utils.collections.onlinePlayerMapOf
import dev.racci.minix.api.utils.kotlin.invokeIfNotNull
import dev.racci.minix.api.utils.minecraft.PlayerUtils
import dev.racci.minix.core.data.PlayerData
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID
import kotlin.reflect.KClass

class PlayerServiceImpl(override val plugin: Minix) : Extension<Minix>(), PlayerService {

    override val name = "Player Manager"

    override val bindToKClass: KClass<*>
        get() = PlayerService::class

    private val playerData = HashMap<UUID, PlayerData>()
    override val inputCallbacks by lazy { onlinePlayerMapOf<PlayerUtils.ChatInput>() }
    override val functionsQuit by lazy { onlinePlayerMapOf<PlayerUtils.PlayerCallback<Unit>>() }
    override val functionsMove by lazy { onlinePlayerMapOf<PlayerUtils.PlayerCallback<Boolean>>() }

    override fun remove(uuid: UUID): Boolean = playerData.remove(uuid) != null

    operator fun get(uuid: UUID) = playerData.computeIfAbsent(uuid) { PlayerData(uuid).also { it.inAccess++ } }

    override fun minusAssign(uuid: UUID) {
        remove(uuid)
    }

    override suspend fun handleEnable() {
        event<PlayerQuitEvent> { playerData[player.uniqueId]?.inAccess?.minus(1) }

        event<AsyncChatEvent>(ignoreCancelled = true) {
            val input = inputCallbacks.remove(player)
            input?.callback?.invoke(player, message()).invokeIfNotNull { cancel() }
        }

        event<PlayerMoveEvent>(ignoreCancelled = true) {
            if (displaced && functionsMove[player]?.run { callback.invoke(player) } == true) {
                cancel()
            }
        }
    }

    override suspend fun handleUnload() {

        for (collection in listOf(inputCallbacks, functionsQuit, functionsMove)) {
            collection.keys.forEach(collection::remove)
        }

        playerData.clear()
    }
}
