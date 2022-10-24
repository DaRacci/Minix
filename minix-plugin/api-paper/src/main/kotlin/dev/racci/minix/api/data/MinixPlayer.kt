package dev.racci.minix.api.data

import dev.racci.minix.api.exceptions.WrappingException
import dev.racci.minix.api.extensions.offlinePlayer
import dev.racci.minix.api.extensions.reflection.safeCast
import net.kyori.adventure.text.Component
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.UUID

public actual class MinixPlayer internal constructor(internal val actualPlayer: OfflinePlayer) : PlatformPlayerData() {
    internal constructor(uuid: UUID) : this(offlinePlayer(uuid))

    public val onlinePlayer: Player get() = actualPlayer.player ?: throw IllegalStateException("Player is not online")

    public actual val uuid: UUID get() = actualPlayer.uniqueId
    public actual val name: String get() = actualPlayer.name ?: uuid.toString()
    public actual val displayName: Component get() = actualPlayer.safeCast<Player>()?.displayName() ?: Component.text(name)
    public actual val isOnline: Boolean get() = actualPlayer.isOnline

    public actual companion object {
        public actual fun wrapped(obj: Any): MinixPlayer {
            return when (obj) {
                is Player,
                is OfflinePlayer -> MinixPlayer(obj as OfflinePlayer)
                is UUID -> MinixPlayer(obj)
                else -> throw WrappingException(null, obj, Player::class, OfflinePlayer::class, UUID::class)
            }
        }
    }
}
