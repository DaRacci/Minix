package dev.racci.minix.data.structs.minecraft

import kotlin.math.sqrt

public data class BlockPos(
    val x: Double,
    val y: Double,
    val z: Double
) : VectorComparable<BlockPos> {

    public constructor(
        x: Int,
        y: Int,
        z: Int
    ) : this(x.toDouble(), y.toDouble(), z.toDouble())

    override fun axis(): DoubleArray = doubleArrayOf(x, y, z)

    override fun factor(
        axis: IntArray
    ): BlockPos = BlockPos(axis[0], axis[1], axis[2])

    public fun asLocationPos(): LocationPos = LocationPos(x, y, z, 0f, 0f)
    public fun asChunkPos(): ChunkPos = ChunkPos(x.toInt() shr 4, z.toInt() shr 4)

    override fun toString(): String = "BlockPos(x=$x, y=$y, z=$z)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlockPos

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    override fun compareTo(other: BlockPos): Int {
        val x = x.compareTo(other.x)
        if (x != 0) return x
        val y = y.compareTo(other.y)
        if (y != 0) return y
        return z.compareTo(other.z)
    }
}

/** Constructs a [BlockPos] of these coordinates. */
public fun blockPosOf(
    x: Int,
    y: Int,
    z: Int
): BlockPos = BlockPos(x, y, z)

/** Constructs a [BlockPos] of these coordinates. */
public fun blockPosOf(
    x: Double,
    y: Double,
    z: Double
): BlockPos = BlockPos(x, y, z)

public fun calculatePythagoras(vararg positions: Pair<Double, Double>): Pair<Double, Double> {
    val pow = positions.map { (x1, x2) -> x1 * x1 to x2 * x2 }

    val x1Sum = pow.sumOf { (x, _) -> x }
    val x2Sum = pow.sumOf { (_, x) -> x }

    val d1 = sqrt(x1Sum)
    val d2 = sqrt(x2Sum)

    return d1 to d2
}
