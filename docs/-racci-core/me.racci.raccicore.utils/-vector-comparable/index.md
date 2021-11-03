//[RacciCore](../../../index.md)/[me.racci.raccicore.utils](../index.md)/[VectorComparable](index.md)

# VectorComparable

[jvm]\
interface [VectorComparable](index.md)&lt;[T](index.md) : [VectorComparable](index.md)&lt;[T](index.md)&gt;&gt; : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparable/index.html)&lt;[T](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [axis](axis.md) | [jvm]<br>abstract fun [axis](axis.md)(): [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html) |
| [compareTo](compare-to.md) | [jvm]<br>open operator override fun [compareTo](compare-to.md)(other: [T](index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [factor](factor.md) | [jvm]<br>abstract fun [factor](factor.md)(axis: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)): [T](index.md) |
| [rangeTo](range-to.md) | [jvm]<br>open operator fun [rangeTo](range-to.md)(other: [T](index.md)): [PosRange](../-pos-range/index.md)&lt;[T](index.md), [T](index.md)&gt; |

## Inheritors

| Name |
|---|
| [BlockPos](../-block-pos/index.md) |
| [LocationPos](../-location-pos/index.md) |
| [ChunkPos](../-chunk-pos/index.md) |
