package dev.racci.minix.api.data

import dev.racci.minix.api.data.enums.LiquidType
import dev.racci.minix.api.exceptions.WrappingException
import dev.racci.minix.api.extensions.offlinePlayer
import dev.racci.minix.api.extensions.reflection.safeCast
import dev.racci.minix.api.services.PlayerService
import dev.racci.minix.api.wrappers.WrapperCompanion
import dev.racci.minix.data.extensions.doubleAccessWatcher
import net.kyori.adventure.text.Component
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.UUID
import kotlin.properties.Delegates

public actual class MinixPlayer constructor(internal val actualPlayer: OfflinePlayer) : PlatformPlayerData() {
    internal constructor(uuid: UUID) : this(offlinePlayer(uuid))

    public val onlinePlayer: Player get() = actualPlayer.player ?: throw IllegalStateException("Player is not online")

    public actual val uuid: UUID get() = actualPlayer.uniqueId
    public actual val name: String get() = actualPlayer.name ?: uuid.toString()
    public actual val displayName: Component get() = actualPlayer.safeCast<Player>()?.displayName() ?: Component.text(name)
    public actual val isOnline: Boolean get() = actualPlayer.isOnline

    public val isDoubleOffhand: Boolean by Delegates.doubleAccessWatcher()
    public val isDoubleSneak: Boolean by Delegates.doubleAccessWatcher()
    public val isDoubleJump: Boolean by Delegates.doubleAccessWatcher()
    public val isDoubleAttack: Boolean by Delegates.doubleAccessWatcher()
    public val isDoubleInteract: Boolean by Delegates.doubleAccessWatcher()

    public var liquidType: LiquidType by Delegates.notNull()

    // TODO -> Better caching
    public actual companion object : WrapperCompanion<MinixPlayer> {
        internal actual val EMPTY: MinixPlayer = MinixPlayer(UUID(0, 0))

        public actual override fun wrapped(obj: Any): MinixPlayer = PlayerService[obj]

        internal actual fun of(obj: Any): MinixPlayer {
            return when (obj) {
                is Player,
                is OfflinePlayer -> MinixPlayer(obj as OfflinePlayer)
                is UUID -> MinixPlayer(obj)
                else -> throw WrappingException(null, obj, Player::class, OfflinePlayer::class, UUID::class)
            }
        }
    }
}
