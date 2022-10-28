package dev.racci.minix.api.integrations.regions

import dev.racci.minix.data.structs.minecraft.BlockPos
import dev.racci.minix.integrations.IntegrationManager
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.Optional

public object RegionManager : IntegrationManager<RegionIntegration>() {

    public fun getRegion(
        pos: BlockPos,
        world: World
    ): Optional<Region> = this.getFirstRegistered {
        it.getRegion(pos, world).orElse(null)
    }

    public fun insideRegion(
        pos: BlockPos,
        world: World
    ): Boolean = this.getFirstRegistered {
        it.insideRegion(pos, world)
    }.orElse(false)

    public fun canBuild(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.getFirstRegistered {
        it.canBuild(pos, world, player)
    }.orElse(true)

    public fun canBreak(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.getFirstRegistered {
        it.canBreak(pos, world, player)
    }.orElse(true)

    public fun canInteract(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.getFirstRegistered {
        it.canInteract(pos, world, player)
    }.orElse(true)

    public fun canAttack(
        pos: BlockPos,
        world: World,
        player: Player,
        target: Entity
    ): Boolean = this.getFirstRegistered {
        it.canAttack(pos, world, player, target)
    }.orElse(true)

    public fun ifWilderness(
        pos: BlockPos,
        world: World,
        action: () -> Unit
    ): Boolean = this.getFirstRegistered {
        it.ifWilderness(pos, world, action)
    }.orElse(true) // if no region integration is found, assume wilderness

    public fun ifTrustedInRegion(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean = this.getFirstRegistered {
        it.ifTrustedInRegion(pos, player, action)
    }.orElse(false)

    public fun ifWildernessOrTrusted(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean = this.getFirstRegistered {
        it.ifWildernessOrTrusted(pos, player, action)
    }.orElse(true)
}
