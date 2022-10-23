package dev.racci.minix.data.structs.minecraft

import dev.racci.minix.api.extensions.reflection.castOrThrow
import dev.racci.minix.data.structs.minecraft.range.PosRange
import dev.racci.minix.data.structs.minecraft.range.PosRangeIterator

public interface VectorComparable<T> : Comparable<T> where T : Comparable<T>, T : VectorComparable<T> {

    public fun axis(): DoubleArray
    public fun factor(axis: IntArray): T

    public operator fun rangeTo(other: T): PosRange<T, T> =
        PosRange(this.castOrThrow(), other) { PosRangeIterator(this.castOrThrow(), other, ::factor) }

    override fun compareTo(other: T): Int {
        val selfAxis = axis()
        val otherAxis = other.axis()
        val pairAxis = selfAxis.mapIndexed { index, axis -> axis to otherAxis[index] }
        val (d1, d2) = calculatePythagoras(*pairAxis.toTypedArray())
        return d1.compareTo(d2)
    }
}
