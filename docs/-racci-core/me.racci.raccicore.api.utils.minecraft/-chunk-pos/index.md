---
title: ChunkPos
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.minecraft](../index.html)/[ChunkPos](index.html)



# ChunkPos



[jvm]\
data class [ChunkPos](index.html)(x: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), z: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [VectorComparable](../-vector-comparable/index.html)&lt;[ChunkPos](index.html)&gt;



## Functions


| Name | Summary |
|---|---|
| [axis](axis.html) | [jvm]<br>open override fun [axis](axis.html)(): [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html) |
| [compareTo](index.html#-2116486419%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [compareTo](index.html#-2116486419%2FFunctions%2F863300109)(other: [ChunkPos](index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [factor](factor.html) | [jvm]<br>open override fun [factor](factor.html)(axis: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)): [ChunkPos](index.html) |
| [rangeTo](index.html#2104758597%2FFunctions%2F863300109) | [jvm]<br>open operator fun [rangeTo](index.html#2104758597%2FFunctions%2F863300109)(other: [ChunkPos](index.html)): [PosRange](../-pos-range/index.html)&lt;[ChunkPos](index.html), [ChunkPos](index.html)&gt; |


## Properties


| Name | Summary |
|---|---|
| [x](x.html) | [jvm]<br>var [x](x.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [z](z.html) | [jvm]<br>var [z](z.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |


## Extensions


| Name | Summary |
|---|---|
| [asBukkitChunk](../as-bukkit-chunk.html) | [jvm]<br>fun [ChunkPos](index.html).[asBukkitChunk](../as-bukkit-chunk.html)(world: World): @NotNullChunk |

