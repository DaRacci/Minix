package dev.racci.minix.api.integrations.regions

import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.UUID

public interface Region {
    public val id: Int
    public val name: String
    public val owner: UUID
    public val world: World

    public fun isMember(player: Player): Boolean

    public fun canBuild(player: Player): Boolean

    public fun canBreak(player: Player): Boolean

    public fun canInteract(player: Player): Boolean

    public fun canAttack(
        player: Player,
        target: Entity
    ): Boolean
}
