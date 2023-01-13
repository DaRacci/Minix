package dev.racci.minix.integrations.regions

import arrow.core.Option
import arrow.core.Some
import arrow.core.filterIsInstance
import com.google.common.collect.HashBiMap
import dev.racci.minix.api.extensions.asBukkitLocation
import dev.racci.minix.api.extensions.orFalse
import dev.racci.minix.api.extensions.orTrue
import dev.racci.minix.api.integrations.regions.RegionIntegration
import dev.racci.minix.api.integrations.regions.RegionManager
import dev.racci.minix.api.utils.kotlin.ifFalse
import dev.racci.minix.api.utils.kotlin.ifTrue
import dev.racci.minix.core.plugin.Minix
import dev.racci.minix.data.structs.minecraft.BlockPos
import dev.racci.minix.integrations.annotations.IntegrationManager
import dev.racci.minix.integrations.annotations.IntegrationPlugin
import me.angeschossen.lands.api.LandsIntegration
import me.angeschossen.lands.api.land.LandArea
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

@IntegrationPlugin("Lands")
@IntegrationManager(RegionManager::class)
public class LandsRegionIntegration(plugin: Minix) : RegionIntegration {
    private val integration = LandsIntegration.of(plugin)
    private val areaReference = HashBiMap.create<LandArea, AreaRegion>()

    override fun getRegion(
        pos: BlockPos,
        world: World
    ): Option<AreaRegion> = Option.fromNullable(integration.getArea(pos.asBukkitLocation(world)))
        .filterIsInstance<LandArea>()
        .map { areaReference.computeIfAbsent(it, ::AreaRegion) }

    override fun insideRegion(
        pos: BlockPos,
        world: World
    ): Boolean = getRegion(pos, world) is Some

    override fun canBuild(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = getRegion(pos, world).map { it.canBuild(player) }.orFalse()

    override fun canBreak(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.getRegion(pos, world).map { it.canBreak(player) }.orFalse()

    override fun canInteract(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean = this.getRegion(pos, world).map { it.canInteract(player) }.orFalse()

    override fun canAttack(
        pos: BlockPos,
        world: World,
        player: Player,
        target: Entity
    ): Boolean = this.getRegion(pos, world).map { it.canAttack(player, target) }.orFalse()

    override fun ifWilderness(
        pos: BlockPos,
        world: World,
        action: () -> Unit
    ): Boolean = this.insideRegion(pos, world).ifFalse(action)

    override fun ifTrustedInRegion(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean = getRegion(pos, player.world).map { it.canBuild(player) }.orFalse().ifTrue(action)

    override fun ifWildernessOrTrusted(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean = getRegion(pos, player.world).map { it.isMember(player) }.orTrue().ifTrue(action)
}
