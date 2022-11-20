package dev.racci.minix.api.integrations.regions

import arrow.core.Option
import dev.racci.minix.api.extensions.orFalse
import dev.racci.minix.api.extensions.orTrue
import dev.racci.minix.data.structs.minecraft.BlockPos
import dev.racci.minix.integrations.IntegrationManager
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

public object RegionManager : IntegrationManager<RegionIntegration>() {

    public fun getRegion(
        pos: BlockPos,
        world: World
    ): Option<Region> = this.get().flatMap { it.getRegion(pos, world) }

    public fun insideRegion(
        pos: BlockPos,
        world: World
    ): Boolean = this.get().map {
        it.insideRegion(pos, world)
    }.orFalse()

    public fun canBuild(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.get().map {
        it.canBuild(pos, world, player)
    }.orTrue()

    public fun canBreak(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.get().map {
        it.canBreak(pos, world, player)
    }.orTrue()

    public fun canInteract(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.get().map {
        it.canInteract(pos, world, player)
    }.orTrue()

    public fun canAttack(
        pos: BlockPos,
        world: World,
        player: Player,
        target: Entity
    ): Boolean = this.get().map {
        it.canAttack(pos, world, player, target)
    }.orTrue()

    public fun ifWilderness(
        pos: BlockPos,
        world: World,
        action: () -> Unit
    ): Boolean = this.get().map {
        it.ifWilderness(pos, world, action)
    }.orTrue() // if no region integration is found, assume wilderness

    public fun ifTrustedInRegion(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean = this.get().map {
        it.ifTrustedInRegion(pos, player, action)
    }.orFalse()

    public fun ifWildernessOrTrusted(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean = this.get().map {
        it.ifWildernessOrTrusted(pos, player, action)
    }.orTrue()
}
