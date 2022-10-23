package dev.racci.minix.data.structs.minecraft

public data class LocationPos(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
) : VectorComparable<LocationPos> {

    override fun axis(): DoubleArray = doubleArrayOf(x, y, z)
    override fun factor(axis: IntArray): LocationPos = LocationPos(axis[0].toDouble(), axis[1].toDouble(), axis[2].toDouble(), yaw, pitch)

    public fun asBlockPos(): BlockPos = BlockPos(x.toInt(), y.toInt(), z.toInt())
}

public fun locationPosOf(
    x: Double,
    y: Double,
    z: Double,
    yaw: Float = 0f,
    pitch: Float = 0f
): LocationPos = LocationPos(x, y, z, yaw, pitch)
