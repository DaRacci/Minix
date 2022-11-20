package dev.racci.minix.api.integrations.regions

import org.bukkit.World
import org.bukkit.WorldCreator
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

    companion object {
        internal val NONE: Region = object : Region {
            override val id: Int = -1
            override val name: String = "none"
            override val owner: UUID = UUID.randomUUID()
            override val world: World = WorldCreator("none").createWorld()!!

            override fun canBuild(player: Player): Boolean = true

            override fun canBreak(player: Player): Boolean = true

            override fun canInteract(player: Player): Boolean = true

            override fun canAttack(
                player: Player,
                target: Entity
            ): Boolean = true
        }
    }
}
