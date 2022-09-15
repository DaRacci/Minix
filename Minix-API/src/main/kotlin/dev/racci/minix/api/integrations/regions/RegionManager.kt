package dev.racci.minix.api.integrations.regions

import dev.racci.minix.api.integrations.IntegrationManager
import dev.racci.minix.api.utils.minecraft.BlockPos
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.Optional

object RegionManager : IntegrationManager<RegionIntegration>() {

    fun getRegion(
        pos: BlockPos,
        world: World
    ): Optional<Region> = this.getFirstRegistered {
        it.getRegion(pos, world).orElse(null)
    }

    fun insideRegion(
        pos: BlockPos,
        world: World
    ): Boolean = this.getFirstRegistered {
        it.insideRegion(pos, world)
    }.orElse(false)

    fun canBuild(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.getFirstRegistered {
        it.canBuild(pos, world, player)
    }.orElse(true)

    fun canBreak(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.getFirstRegistered {
        it.canBreak(pos, world, player)
    }.orElse(true)

    fun canInteract(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.getFirstRegistered {
        it.canInteract(pos, world, player)
    }.orElse(true)

    fun canAttack(
        pos: BlockPos,
        world: World,
        player: Player,
        target: Entity
    ): Boolean = this.getFirstRegistered {
        it.canAttack(pos, world, player, target)
    }.orElse(true)

    fun ifWilderness(
        pos: BlockPos,
        world: World,
        action: () -> Unit
    ): Boolean = this.getFirstRegistered {
        it.ifWilderness(pos, world, action)
    }.orElse(true) // if no region integration is found, assume wilderness

    fun ifTrustedInRegion(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean = this.getFirstRegistered {
        it.ifTrustedInRegion(pos, player, action)
    }.orElse(false)

    fun ifWildernessOrTrusted(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean = this.getFirstRegistered {
        it.ifWildernessOrTrusted(pos, player, action)
    }.orElse(true)
}
