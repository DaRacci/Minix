package dev.racci.minix.api.integrations.regions

import arrow.core.Option
import dev.racci.minix.data.structs.minecraft.BlockPos
import dev.racci.minix.integrations.Integration
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

public interface RegionIntegration : Integration {

    /** @return The [Region] at the given position, if any. */
    public fun getRegion(
        pos: BlockPos,
        world: World
    ): Option<Region>

    /** @return True, if there is a region at the given position. */
    public fun insideRegion(
        pos: BlockPos,
        world: World
    ): Boolean

    /** @return If the given [player] is able to build at the given [pos]. */
    public fun canBuild(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean

    /** @return If the given [player] is able to break at the given [pos]. */
    public fun canBreak(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean

    /**
     * If the player is able to interact with the given [pos].
     * This includes all interactions: opening chests, doors, etc.
     *
     * @param pos The position to check.
     * @param world The world the position is in.
     * @param player The player to check.
     * @return If the player is able to interact with the given [pos].
     */
    public fun canInteract(
        pos: BlockPos,
        world: World,
        player: Player
    ): Boolean

    /** @return If the given [player] is able to attack the [target] the given [pos]. */
    public fun canAttack(
        pos: BlockPos,
        world: World,
        player: Player,
        target: Entity
    ): Boolean

    /** @return Runs the action if the pos isn't inside a region. */
    public fun ifWilderness(
        pos: BlockPos,
        world: World,
        action: () -> Unit
    ): Boolean

    /** @return Runs the action if the player is trusted inside the region at this pos. */
    public fun ifTrustedInRegion(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean

    /** @return Runs the action if the player is a member inside the region at this pos or if the pos is in the wilderness. */
    public fun ifWildernessOrTrusted(
        pos: BlockPos,
        player: Player,
        action: () -> Unit
    ): Boolean
}
