package dev.racci.minix.data.structs.minecraft

import org.bukkit.Chunk
import org.bukkit.World

public data class ChunkPos(
    val x: Int,
    val z: Int
) : VectorComparable<ChunkPos> {

    override fun axis(): DoubleArray = doubleArrayOf(x.toDouble(), z.toDouble())
    override fun factor(axis: IntArray): ChunkPos = ChunkPos(axis[0], axis[1])

    public fun asBukkitChunk(world: World): Chunk = world.getChunkAt(x, z)
}

public fun chunkPosOf(
    x: Int,
    z: Int
): ChunkPos = ChunkPos(x, z)
