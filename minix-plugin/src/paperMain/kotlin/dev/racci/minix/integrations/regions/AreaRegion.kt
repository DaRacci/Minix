package dev.racci.minix.integrations.regions

import dev.racci.minix.api.integrations.regions.Region
import me.angeschossen.lands.api.flags.type.Flags
import me.angeschossen.lands.api.land.LandArea
import org.bukkit.World
import org.bukkit.entity.Animals
import org.bukkit.entity.Entity
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import java.util.UUID

@[JvmInline Suppress("OVERRIDE_BY_INLINE")]
public value class AreaRegion(public val area: LandArea) : Region {
    override inline val id: Int get() = area.land.id
    override inline val name: String get() = area.name
    override inline val owner: UUID get() = area.ownerUID
    override inline val world: World get() = area.world!!

    override fun isMember(player: Player): Boolean = area.isTrusted(player.uniqueId)

    override fun canBuild(player: Player): Boolean = area.hasRoleFlag(player.uniqueId, Flags.BLOCK_PLACE)

    override fun canBreak(player: Player): Boolean = area.hasRoleFlag(player.uniqueId, Flags.BLOCK_BREAK)

    override fun canInteract(player: Player): Boolean = area.hasRoleFlag(player.uniqueId, Flags.INTERACT_GENERAL)

    override fun canAttack(
        player: Player,
        target: Entity
    ): Boolean = when (target) {
        is Player -> area.hasRoleFlag(player.uniqueId, Flags.ATTACK_PLAYER)
        is Animals -> area.hasRoleFlag(player.uniqueId, Flags.ATTACK_ANIMAL)
        is Monster -> area.hasRoleFlag(player.uniqueId, Flags.ATTACK_MONSTER)
        else -> false
    }
}
