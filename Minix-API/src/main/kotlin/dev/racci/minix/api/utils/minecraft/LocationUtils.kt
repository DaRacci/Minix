@file:Suppress("UNUSED", "UNCHECKED_CAST")

package dev.racci.minix.api.utils.minecraft

import net.minecraft.world.phys.Vec3
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import kotlin.math.sqrt

fun blockPosOf(
    x: Int,
    y: Int,
    z: Int,
) = BlockPos(x, y, z)

fun locationPosOf(
    x: Double,
    y: Double,
    z: Double,
    yaw: Float = 0f,
    pitch: Float = 0f,
) = LocationPos(x, y, z, yaw, pitch)

fun chunkPosOf(
    x: Int,
    z: Int,
) = ChunkPos(x, z)

fun Block.asPos() = BlockPos(x, y, z)

fun Location.asBlockPos() = BlockPos(blockX, blockY, blockZ)

fun Location.asPos() = LocationPos(x, y, z, yaw, pitch)

fun Chunk.asPos() = ChunkPos(x, z)

data class BlockPos(
    val x: Int,
    val y: Int,
    val z: Int,
) : VectorComparable<BlockPos> {

    override fun axis(): DoubleArray = doubleArrayOf(x.toDouble(), y.toDouble(), z.toDouble())
    override fun factor(axis: IntArray) = BlockPos(axis[0], axis[1], axis[2])

    fun asBukkitBlock(world: World) = world.getBlockAt(x, y, z)

    fun asBukkitLocation(world: World) = Location(world, x.toDouble(), y.toDouble(), z.toDouble())

    fun asLocationPos() = LocationPos(x.toDouble(), y.toDouble(), z.toDouble(), 0f, 0f)

    fun asChunkPos() = ChunkPos(x shr 4, z shr 4)

    fun toVec3() = Vec3(x.toDouble(), y.toDouble(), z.toDouble())
}

data class LocationPos(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
) : VectorComparable<LocationPos> {

    override fun axis(): DoubleArray = doubleArrayOf(x, y, z)
    override fun factor(axis: IntArray) =
        LocationPos(axis[0].toDouble(), axis[1].toDouble(), axis[2].toDouble(), yaw, pitch)

    fun LocationPos.asBukkitBlock(world: World) = world.getBlockAt(x.toInt(), y.toInt(), z.toInt())

    fun LocationPos.asBukkitLocation(world: World) = Location(world, x, y, z)

    fun LocationPos.asBlockPos() = BlockPos(x.toInt(), y.toInt(), z.toInt())
}

data class ChunkPos(
    val x: Int,
    val z: Int,
) : VectorComparable<ChunkPos> {

    override fun axis(): DoubleArray = doubleArrayOf(x.toDouble(), z.toDouble())
    override fun factor(axis: IntArray) = ChunkPos(axis[0], axis[1])

    fun asBukkitChunk(world: World) = world.getChunkAt(x, z)
}

interface VectorComparable<T : VectorComparable<T>> : Comparable<T> {

    fun axis(): DoubleArray
    fun factor(axis: IntArray): T

    operator fun rangeTo(other: T): PosRange<T, T> =
        PosRange(this as T, other) { PosRangeIterator(this, other, ::factor) }

    override fun compareTo(other: T): Int {
        val selfAxis = axis()
        val otherAxis = other.axis()
        val pairAxis = selfAxis.mapIndexed { index, axis -> axis to otherAxis[index] }
        val (d1, d2) = calculatePythagoras(*pairAxis.toTypedArray())
        return d1.compareTo(d2)
    }
}

fun Pair<Int, Int>.toDouble() = first.toDouble() to second.toDouble()

fun Pair<Double, Double>.toInt() = first.toInt() to second.toInt()

fun calculatePythagoras(vararg positions: Pair<Double, Double>): Pair<Double, Double> {
    val pow = positions.map { (x1, x2) -> x1 * x1 to x2 * x2 }

    val x1Sum = pow.sumOf { (x, _) -> x }
    val x2Sum = pow.sumOf { (_, x) -> x }

    val d1 = sqrt(x1Sum)
    val d2 = sqrt(x2Sum)

    return d1 to d2
}
