//[RacciCore](../../../index.md)/[me.racci.raccicore.utils](../index.md)/[LocationPos](index.md)

# LocationPos

[jvm]\
data class [LocationPos](index.md)(x: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), y: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), z: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), yaw: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), pitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)) : [VectorComparable](../-vector-comparable/index.md)&lt;[LocationPos](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [axis](axis.md) | [jvm]<br>open override fun [axis](axis.md)(): [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html) |
| [compareTo](index.md#-1818978705%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [compareTo](index.md#-1818978705%2FFunctions%2F-1216412040)(other: [LocationPos](index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [factor](factor.md) | [jvm]<br>open override fun [factor](factor.md)(axis: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)): [LocationPos](index.md) |
| [rangeTo](index.md#1222944855%2FFunctions%2F-1216412040) | [jvm]<br>open operator fun [rangeTo](index.md#1222944855%2FFunctions%2F-1216412040)(other: [LocationPos](index.md)): [PosRange](../-pos-range/index.md)&lt;[LocationPos](index.md), [LocationPos](index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [pitch](pitch.md) | [jvm]<br>val [pitch](pitch.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [x](x.md) | [jvm]<br>var [x](x.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [y](y.md) | [jvm]<br>var [y](y.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [yaw](yaw.md) | [jvm]<br>val [yaw](yaw.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [z](z.md) | [jvm]<br>var [z](z.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |

## Extensions

| Name | Summary |
|---|---|
| [asBlockPos](../as-block-pos.md) | [jvm]<br>fun [LocationPos](index.md).[asBlockPos](../as-block-pos.md)(): [BlockPos](../-block-pos/index.md) |
| [asBukkitBlock](../as-bukkit-block.md) | [jvm]<br>fun [LocationPos](index.md).[asBukkitBlock](../as-bukkit-block.md)(world: World): @NotNullBlock |
| [asBukkitLocation](../as-bukkit-location.md) | [jvm]<br>fun [LocationPos](index.md).[asBukkitLocation](../as-bukkit-location.md)(world: World): Location |
