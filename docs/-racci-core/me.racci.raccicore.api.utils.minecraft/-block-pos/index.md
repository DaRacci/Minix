---
title: BlockPos
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.minecraft](../index.html)/[BlockPos](index.html)



# BlockPos



[jvm]\
data class [BlockPos](index.html)(x: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), y: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), z: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [VectorComparable](../-vector-comparable/index.html)&lt;[BlockPos](index.html)&gt;



## Functions


| Name | Summary |
|---|---|
| [axis](axis.html) | [jvm]<br>open override fun [axis](axis.html)(): [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html) |
| [compareTo](index.html#516437261%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [compareTo](index.html#516437261%2FFunctions%2F863300109)(other: [BlockPos](index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [factor](factor.html) | [jvm]<br>open override fun [factor](factor.html)(axis: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)): [BlockPos](index.html) |
| [rangeTo](index.html#442714981%2FFunctions%2F863300109) | [jvm]<br>open operator fun [rangeTo](index.html#442714981%2FFunctions%2F863300109)(other: [BlockPos](index.html)): [PosRange](../-pos-range/index.html)&lt;[BlockPos](index.html), [BlockPos](index.html)&gt; |


## Properties


| Name | Summary |
|---|---|
| [x](x.html) | [jvm]<br>var [x](x.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [y](y.html) | [jvm]<br>var [y](y.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [z](z.html) | [jvm]<br>var [z](z.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |


## Extensions


| Name | Summary |
|---|---|
| [asBukkitBlock](../as-bukkit-block.html) | [jvm]<br>fun [BlockPos](index.html).[asBukkitBlock](../as-bukkit-block.html)(world: World): @NotNullBlock |
| [asBukkitLocation](../as-bukkit-location.html) | [jvm]<br>fun [BlockPos](index.html).[asBukkitLocation](../as-bukkit-location.html)(world: World): Location |
| [asChunkPos](../as-chunk-pos.html) | [jvm]<br>fun [BlockPos](index.html).[asChunkPos](../as-chunk-pos.html)(): [ChunkPos](../-chunk-pos/index.html) |
| [asLocationPos](../as-location-pos.html) | [jvm]<br>fun [BlockPos](index.html).[asLocationPos](../as-location-pos.html)(): [LocationPos](../-location-pos/index.html) |

