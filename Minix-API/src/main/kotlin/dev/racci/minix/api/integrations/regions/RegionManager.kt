package dev.racci.minix.api.integrations.regions

import dev.racci.minix.api.integrations.IntegrationManager
import dev.racci.minix.api.utils.minecraft.BlockPos
import dev.racci.minix.api.utils.minecraft.ChunkPos
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.Optional

object RegionManager : IntegrationManager<RegionIntegration>() {

    fun getRegion(
        pos: ChunkPos,
        world: World
    ): Optional<Region> = this.getFirstRegistered {
        it.getRegion(pos, world).orElse(null)
    }

    fun insideRegion(
        pos: ChunkPos,
        world: World
    ): Boolean = this.getFirstRegistered {
        it.insideRegion(pos, world)
    }.orElse(false)

    fun ifWilderness(
        pos: ChunkPos,
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
}
