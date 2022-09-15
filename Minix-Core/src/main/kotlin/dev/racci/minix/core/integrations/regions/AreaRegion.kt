package dev.racci.minix.core.integrations.regions

import dev.racci.minix.api.integrations.regions.Region
import me.angeschossen.lands.api.flags.Flags
import me.angeschossen.lands.api.land.LandArea
import org.bukkit.entity.Animals
import org.bukkit.entity.Entity
import org.bukkit.entity.Monster
import org.bukkit.entity.Player

@[JvmInline Suppress("OVERRIDE_BY_INLINE")]
value class AreaRegion(val area: LandArea) : Region {
    override inline val id get() = area.land.id
    override inline val name get() = area.name
    override inline val owner get() = area.ownerUID
    override inline val world get() = area.world!!

    override fun canBuild(player: Player) = area.hasFlag(player.uniqueId, Flags.BLOCK_PLACE)

    override fun canBreak(player: Player) = area.hasFlag(player.uniqueId, Flags.BLOCK_BREAK)

    override fun canInteract(player: Player) = area.hasFlag(player.uniqueId, Flags.INTERACT_GENERAL)

    override fun canAttack(
        player: Player,
        target: Entity
    ) = when (target) {
        is Player -> area.hasFlag(player.uniqueId, Flags.ATTACK_PLAYER)
        is Animals -> area.hasFlag(player.uniqueId, Flags.ATTACK_ANIMAL)
        is Monster -> area.hasFlag(player.uniqueId, Flags.ATTACK_MONSTER)
        else -> false
    }
}
