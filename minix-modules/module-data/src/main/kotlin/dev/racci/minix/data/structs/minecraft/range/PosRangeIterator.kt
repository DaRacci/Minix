package dev.racci.minix.data.structs.minecraft.range

import dev.racci.minix.data.structs.minecraft.VectorComparable

public class PosRangeIterator<T : VectorComparable<T>>(
    first: T,
    last: T,
    public val factor: (axis: IntArray) -> T
) : Iterator<T> {

    private val firstAxis = first.axis()
    private val lastAxis = last.axis()
    private val closedAxisRanges = firstAxis.mapIndexed { index, value ->
        IntProgression.fromClosedRange(value.toInt(), lastAxis[index].toInt(), 1)
    }
    private val iteratorAxis = closedAxisRanges.map { it.iterator() }.toTypedArray()

    private val actualAxis = iteratorAxis.toList().subList(0, iteratorAxis.size - 1)
        .map { it.nextInt() }
        .toTypedArray()

    override fun hasNext(): Boolean = iteratorAxis.any { it.hasNext() }

    override fun next(): T {
        val lastIndex = iteratorAxis.size - 1
        val last = iteratorAxis[lastIndex]
        if (last.hasNext()) {
            val axis = IntArray(actualAxis.size) { actualAxis[it] } + last.nextInt()
            return factor(axis)
        }
        for (i in lastIndex - 1 downTo 0) {
            val axis = iteratorAxis[i]
            if (axis.hasNext()) {
                actualAxis[i] = axis.nextInt()
                iteratorAxis[i + 1] = closedAxisRanges[i + 1].iterator()
                break
            }
        }
        return next()
    }
}
