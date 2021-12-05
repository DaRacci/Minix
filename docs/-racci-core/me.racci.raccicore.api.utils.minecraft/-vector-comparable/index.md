---
title: VectorComparable
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.minecraft](../index.html)/[VectorComparable](index.html)



# VectorComparable



[jvm]\
interface [VectorComparable](index.html)&lt;[T](index.html) : [VectorComparable](index.html)&lt;[T](index.html)&gt;&gt; : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparable/index.html)&lt;[T](index.html)&gt;



## Functions


| Name | Summary |
|---|---|
| [axis](axis.html) | [jvm]<br>abstract fun [axis](axis.html)(): [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html) |
| [compareTo](compare-to.html) | [jvm]<br>open operator override fun [compareTo](compare-to.html)(other: [T](index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [factor](factor.html) | [jvm]<br>abstract fun [factor](factor.html)(axis: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)): [T](index.html) |
| [rangeTo](range-to.html) | [jvm]<br>open operator fun [rangeTo](range-to.html)(other: [T](index.html)): [PosRange](../-pos-range/index.html)&lt;[T](index.html), [T](index.html)&gt; |


## Inheritors


| Name |
|---|
| [BlockPos](../-block-pos/index.html) |
| [LocationPos](../-location-pos/index.html) |
| [ChunkPos](../-chunk-pos/index.html) |

