---
title: LocationPos
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.minecraft](../index.html)/[LocationPos](index.html)



# LocationPos



[jvm]\
data class [LocationPos](index.html)(x: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), y: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), z: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), yaw: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), pitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)) : [VectorComparable](../-vector-comparable/index.html)&lt;[LocationPos](index.html)&gt;



## Functions


| Name | Summary |
|---|---|
| [axis](axis.html) | [jvm]<br>open override fun [axis](axis.html)(): [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html) |
| [compareTo](index.html#786409615%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [compareTo](index.html#786409615%2FFunctions%2F863300109)(other: [LocationPos](index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [factor](factor.html) | [jvm]<br>open override fun [factor](factor.html)(axis: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)): [LocationPos](index.html) |
| [rangeTo](index.html#-745745609%2FFunctions%2F863300109) | [jvm]<br>open operator fun [rangeTo](index.html#-745745609%2FFunctions%2F863300109)(other: [LocationPos](index.html)): [PosRange](../-pos-range/index.html)&lt;[LocationPos](index.html), [LocationPos](index.html)&gt; |


## Properties


| Name | Summary |
|---|---|
| [pitch](pitch.html) | [jvm]<br>val [pitch](pitch.html): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [x](x.html) | [jvm]<br>var [x](x.html): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [y](y.html) | [jvm]<br>var [y](y.html): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |
| [yaw](yaw.html) | [jvm]<br>val [yaw](yaw.html): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [z](z.html) | [jvm]<br>var [z](z.html): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html) |


## Extensions


| Name | Summary |
|---|---|
| [asBlockPos](../as-block-pos.html) | [jvm]<br>fun [LocationPos](index.html).[asBlockPos](../as-block-pos.html)(): [BlockPos](../-block-pos/index.html) |
| [asBukkitBlock](../as-bukkit-block.html) | [jvm]<br>fun [LocationPos](index.html).[asBukkitBlock](../as-bukkit-block.html)(world: World): @NotNullBlock |
| [asBukkitLocation](../as-bukkit-location.html) | [jvm]<br>fun [LocationPos](index.html).[asBukkitLocation](../as-bukkit-location.html)(world: World): Location |

