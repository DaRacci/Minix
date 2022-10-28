package dev.racci.minix.integrations.regions

import com.google.common.collect.HashBiMap
import dev.racci.minix.api.extensions.asBukkitLocation
import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.api.integrations.regions.Region
import dev.racci.minix.api.integrations.regions.RegionIntegration
import dev.racci.minix.api.integrations.regions.RegionManager
import dev.racci.minix.api.utils.kotlin.ifFalse
import dev.racci.minix.api.utils.kotlin.ifTrue
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.data.structs.minecraft.BlockPos
import dev.racci.minix.integrations.annotations.MappedIntegration
import me.angeschossen.lands.api.integration.LandsIntegration
import me.angeschossen.lands.api.land.LandArea
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.Optional

@MappedIntegration("Lands", RegionManager::class)
public class LandsRegionIntegration(plugin: Minix) : RegionIntegration {
    private val integration = LandsIntegration(plugin)
    private val areaReference = HashBiMap.create<LandArea, AreaRegion>()

    override fun getRegion(
        pos: BlockPos,
        world: World
    ): Optional<Region> {
        val land = this.integration.getLand(world, pos.x.toInt(), pos.z.toInt())
        if (land == null || !land.exists()) return Optional.empty()

        val area = land.getArea(pos.asBukkitLocation(world)).castOrThrow<LandArea>()
        val areaRegion = areaReference.computeIfAbsent(area, ::AreaRegion)
        return Optional.of(areaRegion)
    }

    override fun insideRegion(
        pos: BlockPos,
        world: World
    ): Boolean = this.integration.isClaimed(world, pos.x.toInt(), pos.z.toInt())

    override fun canBuild(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.getRegion(pos, world).map { it.canBuild(player) }.orElse(false)

    override fun canBreak(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.getRegion(pos, world).map { it.canBreak(player) }.orElse(false)

    override fun canInteract(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.getRegion(pos, world).map { it.canInteract(player) }.orElse(false)

    override fun canAttack(
        pos: BlockPos,
        world: World,
        player: Player,
        target: Entity
    ): Boolean = this.getRegion(pos, world).map { it.canAttack(player, target) }.orElse(false)

    override fun ifWilderness(
        pos: BlockPos,
        world: World,
        action: () -> Unit
    ): Boolean = this.insideRegion(pos, world).ifFalse(action)

    override fun ifTrustedInRegion(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean {
        val area = this.integration.getArea(player.world, pos.x.toInt(), pos.y.toInt(), pos.z.toInt())
        return area != null && area.isTrusted(player.uniqueId).ifFalse(action)
    }

    override fun ifWildernessOrTrusted(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean {
        val area = this.integration.getArea(player.world, pos.x.toInt(), pos.y.toInt(), pos.z.toInt())
        return (area == null || area.isTrusted(player.uniqueId)).ifTrue(action)
    }
}
