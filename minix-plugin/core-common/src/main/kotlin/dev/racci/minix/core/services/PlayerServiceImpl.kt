package dev.racci.minix.core.services

import dev.racci.minix.api.annotations.MappedExtension
import dev.racci.minix.api.callbacks.PlayerMoveCallback
import dev.racci.minix.api.callbacks.PlayerQuitCallback
import dev.racci.minix.api.collections.player.OnlinePlayerMap
import dev.racci.minix.api.data.MinixPlayer
import dev.racci.minix.api.extension.Extension
import dev.racci.minix.api.extension.ExtensionCompanion
import dev.racci.minix.api.services.PlayerService
import dev.racci.minix.api.utils.minecraft.PlayerUtils
import dev.racci.minix.core.plugin.Minix
import java.util.UUID

@MappedExtension(bindToKClass = PlayerService::class)
public expect class PlayerServiceImpl internal constructor() : Extension<Minix>, PlayerService {
    override val inputCallbacks: OnlinePlayerMap<PlayerUtils.ChatInput>
    override val quitCallbacks: OnlinePlayerMap<PlayerQuitCallback>
    override val moveCallbacks: OnlinePlayerMap<PlayerMoveCallback>

    override operator fun get(obj: Any): MinixPlayer

    override fun remove(uuid: UUID): Boolean

    override fun minusAssign(obj: Any)

    override suspend fun handleEnable()

    override suspend fun handleUnload()

    public companion object : ExtensionCompanion<PlayerServiceImpl>
}
