package dev.racci.minix.api.integrations.regions

import dev.racci.minix.api.integrations.Integration
import dev.racci.minix.api.utils.minecraft.BlockPos
import dev.racci.minix.api.utils.minecraft.ChunkPos
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.Optional

interface RegionIntegration : Integration {

    /** @return The [Region] at the given position, if any. */
    fun getRegion(
        pos: ChunkPos,
        world: World
    ): Optional<Region>

    /** @return True, if there is a region at the given position. */
    fun insideRegion(
        pos: ChunkPos,
        world: World
    ): Boolean

    /** @return Runs the action if the pos isn't inside a region. */
    fun ifWilderness(
        pos: ChunkPos,
        world: World,
        action: () -> Unit
    ): Boolean

    /** @return Runs the action if the player is trusted inside the region at this pos. */
    fun ifTrustedInRegion(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean

    /** @return Runs the action if the player is a member inside the region at this pos or if the pos is in the wilderness. */
    fun ifWildernessOrTrusted(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean
}
