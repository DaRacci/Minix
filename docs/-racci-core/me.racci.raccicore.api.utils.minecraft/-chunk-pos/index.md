//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.minecraft](../index.md)/[ChunkPos](index.md)

# ChunkPos

[jvm]\
data class [ChunkPos](index.md)(x: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), z: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [VectorComparable](../-vector-comparable/index.md)&lt;[ChunkPos](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [axis](axis.md) | [jvm]<br>open override fun [axis](axis.md)(): [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html) |
| [compareTo](index.md#-2116486419%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [compareTo](index.md#-2116486419%2FFunctions%2F-1216412040)(other: [ChunkPos](index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [factor](factor.md) | [jvm]<br>open override fun [factor](factor.md)(axis: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)): [ChunkPos](index.md) |
| [rangeTo](index.md#2104758597%2FFunctions%2F-1216412040) | [jvm]<br>open operator fun [rangeTo](index.md#2104758597%2FFunctions%2F-1216412040)(other: [ChunkPos](index.md)): [PosRange](../-pos-range/index.md)&lt;[ChunkPos](index.md), [ChunkPos](index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [x](x.md) | [jvm]<br>var [x](x.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [z](z.md) | [jvm]<br>var [z](z.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Extensions

| Name | Summary |
|---|---|
| [asBukkitChunk](../as-bukkit-chunk.md) | [jvm]<br>fun [ChunkPos](index.md).[asBukkitChunk](../as-bukkit-chunk.md)(world: World): @NotNullChunk |
