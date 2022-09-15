package dev.racci.minix.api.integrations.regions

import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.UUID

interface Region {
    val id: Int
    val name: String
    val owner: UUID
    val world: World

    fun canBuild(player: Player): Boolean

    fun canBreak(player: Player): Boolean

    fun canInteract(player: Player): Boolean

    fun canAttack(
        player: Player,
        target: Entity
    ): Boolean
}
