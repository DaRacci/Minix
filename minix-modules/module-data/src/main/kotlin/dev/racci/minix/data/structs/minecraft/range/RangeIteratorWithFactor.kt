package dev.racci.minix.data.structs.minecraft.range

import dev.racci.minix.data.structs.minecraft.VectorComparable

public class RangeIteratorWithFactor<T, POS : VectorComparable<POS>>(
    start: T,
    end: T,
    private val factor: (POS) -> T,
    posFactor: (T) -> POS
) : Iterator<T> {

    public val iterator: PosRangeIterator<POS> = PosRangeIterator(posFactor(start), posFactor(end), posFactor(start)::factor)

    override fun hasNext(): Boolean = iterator.hasNext()
    override fun next(): T = factor(iterator.next())
}
