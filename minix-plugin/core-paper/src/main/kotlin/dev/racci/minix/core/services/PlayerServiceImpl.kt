package dev.racci.minix.core.services

import dev.racci.minix.api.PlatformProxy
import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.callbacks.PlayerMoveCallback
import dev.racci.minix.api.callbacks.PlayerQuitCallback
import dev.racci.minix.api.collections.player.OnlinePlayerMap
import dev.racci.minix.api.collections.player.onlinePlayerMapOf
import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.events.player.PlayerUnloadEvent
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionCompanion
import dev.racci.minix.api.extensions.cancel
import dev.racci.minix.api.extensions.collections.clear
import dev.racci.minix.api.extensions.displaced
import dev.racci.minix.api.services.PlayerService
import dev.racci.minix.api.utils.kotlin.invokeIfNotNull
import dev.racci.minix.api.utils.minecraft.PlayerUtils
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.flowbus.FlowBus
import dev.racci.minix.flowbus.subscribe
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.koin.core.component.get
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@MappedExtension(bindToKClass = PlayerService::class)
public actual class PlayerServiceImpl internal actual constructor(actual override val plugin: Minix) : Extension<Minix>(), PlayerService {
    private val playerCache = ConcurrentHashMap<UUID, MinixPlayer>()

    actual override val inputCallbacks: OnlinePlayerMap<PlayerUtils.ChatInput> by lazy(::onlinePlayerMapOf)
    actual override val quitCallbacks: OnlinePlayerMap<PlayerQuitCallback> by lazy(::onlinePlayerMapOf)
    public actual override val moveCallbacks: OnlinePlayerMap<PlayerMoveCallback> by lazy(::onlinePlayerMapOf)
    public actual override operator fun get(obj: Any): MinixPlayer {
        playerCache[PlatformProxy.getUUID(obj)]?.let { return it }
        val wrapped = MinixPlayer.of(obj)

        if (wrapped.isOnline) {
            playerCache[PlatformProxy.getUUID(obj)] = wrapped
            wrapped.accessCount++
        }

        return wrapped
    }

    actual override fun remove(uuid: UUID): Boolean = playerCache.remove(uuid) != null

    public actual override fun minusAssign(obj: Any) {
        remove(PlatformProxy.getUUID(obj))
    }

    actual override suspend fun handleEnable() {
        subscribe<PlayerQuitEvent> {
            playerCache[this.player.uniqueId]?.accessCount?.dec()
        }

        subscribe<AsyncChatEvent>(ignoreCancelled = true) {
            get(player).withAccess { player ->
                val input = inputCallbacks.remove(player)
                input?.callback?.invoke(this.message()).invokeIfNotNull { this.cancel() }
            }
        }

        subscribe<PlayerMoveEvent>(ignoreCancelled = true) {
            get(this.player).withAccess { player ->
                if (displaced && moveCallbacks[player]?.invoke(player) != true) this.cancel()
            }
        }
    }

    actual override suspend fun handleUnload() {
        for (collection in listOf(inputCallbacks, quitCallbacks, moveCallbacks)) {
            collection.keys.forEach(collection::remove)
        }

        val bus = get<FlowBus>()
        playerCache.clear { _, minixPlayer -> bus.post(PlayerUnloadEvent(minixPlayer)) }
    }

    public actual companion object : ExtensionCompanion<PlayerServiceImpl>()
}
