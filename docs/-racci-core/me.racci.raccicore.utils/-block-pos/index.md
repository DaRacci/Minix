//[RacciCore](../../../index.md)/[me.racci.raccicore.utils](../index.md)/[BlockPos](index.md)

# BlockPos

[jvm]\
data class [BlockPos](index.md)(x: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), y: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), z: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [VectorComparable](../-vector-comparable/index.md)&lt;[BlockPos](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [axis](axis.md) | [jvm]<br>open override fun [axis](axis.md)(): [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html) |
| [compareTo](index.md#-1735584979%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [compareTo](index.md#-1735584979%2FFunctions%2F-1216412040)(other: [BlockPos](index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [factor](factor.md) | [jvm]<br>open override fun [factor](factor.md)(axis: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)): [BlockPos](index.md) |
| [rangeTo](index.md#813730373%2FFunctions%2F-1216412040) | [jvm]<br>open operator fun [rangeTo](index.md#813730373%2FFunctions%2F-1216412040)(other: [BlockPos](index.md)): [PosRange](../-pos-range/index.md)&lt;[BlockPos](index.md), [BlockPos](index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [x](x.md) | [jvm]<br>var [x](x.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [y](y.md) | [jvm]<br>var [y](y.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [z](z.md) | [jvm]<br>var [z](z.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Extensions

| Name | Summary |
|---|---|
| [asBukkitBlock](../as-bukkit-block.md) | [jvm]<br>fun [BlockPos](index.md).[asBukkitBlock](../as-bukkit-block.md)(world: World): @NotNullBlock |
| [asBukkitLocation](../as-bukkit-location.md) | [jvm]<br>fun [BlockPos](index.md).[asBukkitLocation](../as-bukkit-location.md)(world: World): Location |
| [asChunkPos](../as-chunk-pos.md) | [jvm]<br>fun [BlockPos](index.md).[asChunkPos](../as-chunk-pos.md)(): [ChunkPos](../-chunk-pos/index.md) |
| [asLocationPos](../as-location-pos.md) | [jvm]<br>fun [BlockPos](index.md).[asLocationPos](../as-location-pos.md)(): [LocationPos](../-location-pos/index.md) |
