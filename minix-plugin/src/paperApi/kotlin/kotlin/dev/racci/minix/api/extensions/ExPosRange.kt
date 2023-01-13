package dev.racci.minix.api.extensions

import dev.racci.minix.data.structs.minecraft.BlockPos
import dev.racci.minix.data.structs.minecraft.ChunkPos
import dev.racci.minix.data.structs.minecraft.range.PosRange
import dev.racci.minix.data.structs.minecraft.range.RangeIteratorWithFactor
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.block.Block

public operator fun PosRange<*, BlockPos>.contains(
    other: Location
): Boolean = contains(other.asPos())

public operator fun PosRange<*, BlockPos>.contains(
    other: Block
): Boolean = contains(other.asPos())

public operator fun PosRange<*, ChunkPos>.contains(
    other: Chunk
): Boolean = contains(other.asPos())

public operator fun Location.rangeTo(
    other: Location
): PosRange<Location, BlockPos> = PosRange(this.asBlockPos(), other.asBlockPos()) {
    RangeIteratorWithFactor<Location, BlockPos>(
        this,
        other,
        { it.asBukkitLocation(world) },
        { it.asBlockPos() }
    )
}

public operator fun Block.rangeTo(
    other: Block
): PosRange<Block, BlockPos> = PosRange(this.asPos(), other.asPos()) {
    RangeIteratorWithFactor<Block, BlockPos>(
        this,
        other,
        { it.asBukkitBlock(world) },
        { it.asPos() }
    )
}

public operator fun Chunk.rangeTo(
    other: Chunk
): PosRange<Chunk, ChunkPos> = PosRange(this.asPos(), other.asPos()) {
    RangeIteratorWithFactor<Chunk, ChunkPos>(
        this,
        other,
        { it.asBukkitChunk(world) },
        { it.asPos() }
    )
}
