//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils](../index.md)/[PlayerComparator](index.md)

# PlayerComparator

[jvm]\
object [PlayerComparator](index.md) : [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Player&gt;

## Functions

| Name | Summary |
|---|---|
| [compare](compare.md) | [jvm]<br>open override fun [compare](compare.md)(p0: Player, p1: Player): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [reversed](index.md#208665987%2FFunctions%2F-1216412040) | [jvm]<br>open fun [reversed](index.md#208665987%2FFunctions%2F-1216412040)(): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Player&gt; |
| [thenComparing](index.md#1905524030%2FFunctions%2F-1216412040) | [jvm]<br>open fun [thenComparing](index.md#1905524030%2FFunctions%2F-1216412040)(p0: [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;in Player&gt;): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Player&gt;<br>open fun &lt;[U](index.md#934963091%2FFunctions%2F-1216412040) : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparable/index.html)&lt;[U](index.md#934963091%2FFunctions%2F-1216412040)&gt;&gt; [thenComparing](index.md#934963091%2FFunctions%2F-1216412040)(p0: [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)&lt;in Player, out [U](index.md#934963091%2FFunctions%2F-1216412040)&gt;): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Player&gt;<br>open fun &lt;[U](index.md#165933595%2FFunctions%2F-1216412040) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [thenComparing](index.md#165933595%2FFunctions%2F-1216412040)(p0: [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)&lt;in Player, out [U](index.md#165933595%2FFunctions%2F-1216412040)&gt;, p1: [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;in [U](index.md#165933595%2FFunctions%2F-1216412040)&gt;): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Player&gt; |
| [thenComparingDouble](index.md#-1673626131%2FFunctions%2F-1216412040) | [jvm]<br>open fun [thenComparingDouble](index.md#-1673626131%2FFunctions%2F-1216412040)(p0: [ToDoubleFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/ToDoubleFunction.html)&lt;in Player&gt;): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Player&gt; |
| [thenComparingInt](index.md#1623279945%2FFunctions%2F-1216412040) | [jvm]<br>open fun [thenComparingInt](index.md#1623279945%2FFunctions%2F-1216412040)(p0: [ToIntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/ToIntFunction.html)&lt;in Player&gt;): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Player&gt; |
| [thenComparingLong](index.md#1879414861%2FFunctions%2F-1216412040) | [jvm]<br>open fun [thenComparingLong](index.md#1879414861%2FFunctions%2F-1216412040)(p0: [ToLongFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/ToLongFunction.html)&lt;in Player&gt;): [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;Player&gt; |
