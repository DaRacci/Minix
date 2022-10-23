package dev.racci.minix.data.structs.minecraft.range

import dev.racci.minix.data.structs.minecraft.VectorComparable

public class PosRange<T, POS : VectorComparable<POS>>(
    public val first: POS,
    public val last: POS,
    public val buildIterator: () -> Iterator<T>
) : ClosedRange<POS>, Iterable<T> {

    override val endInclusive: POS get() = last
    override val start: POS get() = first

    override fun contains(value: POS): Boolean {
        val firstAxis = first.axis()
        val lastAxis = last.axis()
        return value.axis().withIndex().all { (index, it) ->
            it >= firstAxis[index] && it <= lastAxis[index]
        }
    }

    override fun iterator(): Iterator<T> = buildIterator()
}
