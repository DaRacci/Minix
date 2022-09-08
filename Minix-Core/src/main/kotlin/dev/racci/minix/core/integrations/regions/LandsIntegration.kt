package dev.racci.minix.core.integrations.regions

import com.google.common.collect.HashBiMap
import dev.racci.minix.api.annotations.MappedIntegration
import dev.racci.minix.api.integrations.regions.Region
import dev.racci.minix.api.integrations.regions.RegionIntegration
import dev.racci.minix.api.integrations.regions.RegionManager
import dev.racci.minix.api.plugin.Minix
import dev.racci.minix.api.plugin.MinixPlugin
import dev.racci.minix.api.utils.kotlin.ifFalse
import dev.racci.minix.api.utils.kotlin.ifTrue
import dev.racci.minix.api.utils.minecraft.BlockPos
import dev.racci.minix.api.utils.minecraft.ChunkPos
import me.angeschossen.lands.api.integration.LandsIntegration
import me.angeschossen.lands.api.land.Land
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.Optional

@MappedIntegration("Lands", Minix::class, RegionManager::class)
class LandsIntegration(override val pluginRegister: MinixPlugin) : RegionIntegration {
    private val integration: LandsIntegration = LandsIntegration(pluginRegister)
    private val landReference = HashBiMap.create<Land, LandRegion>()

    override fun getRegion(
        pos: ChunkPos,
        world: World
    ): Optional<Region> {
        val land = this.integration.getLand(world, pos.x, pos.z)
        if (land == null || !land.exists()) return Optional.empty()

        val landRegion = this.landReference.computeIfAbsent(land) { LandRegion(it) }
        return Optional.of(landRegion)
    }

    override fun insideRegion(
        pos: ChunkPos,
        world: World
    ): Boolean = this.integration.isClaimed(world, pos.x, pos.z)

    override fun ifWilderness(
        pos: ChunkPos,
        world: World,
        action: () -> Unit
    ): Boolean = this.insideRegion(pos, world).ifFalse(action)

    override fun ifTrustedInRegion(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean {
        val area = this.integration.getArea(player.world, pos.x, pos.y, pos.z)
        return area != null && area.isTrusted(player.uniqueId).ifFalse(action)
    }

    override fun ifWildernessOrTrusted(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean {
        val area = this.integration.getArea(player.world, pos.x, pos.y, pos.z)
        return (area == null || area.isTrusted(player.uniqueId)).ifTrue(action)
    }
}
