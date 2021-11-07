//[RacciCore](../../../index.md)/[me.racci.raccicore.utils](../index.md)/[RangeIteratorWithFactor](index.md)

# RangeIteratorWithFactor

[jvm]\
class [RangeIteratorWithFactor](index.md)&lt;[T](index.md), [POS](index.md) : [VectorComparable](../-vector-comparable/index.md)&lt;[POS](index.md)&gt;&gt;(start: [T](index.md), end: [T](index.md), factor: ([POS](index.md)) -&gt; [T](index.md), posFactor: ([T](index.md)) -&gt; [POS](index.md)) : [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[T](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [forEachRemaining](../../me.racci.raccicore.utils.collections/-observable-mutable-iterator/index.md#-511368593%2FFunctions%2F-1216412040) | [jvm]<br>open fun [forEachRemaining](../../me.racci.raccicore.utils.collections/-observable-mutable-iterator/index.md#-511368593%2FFunctions%2F-1216412040)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [T](index.md)&gt;) |
| [hasNext](has-next.md) | [jvm]<br>open operator override fun [hasNext](has-next.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [next](next.md) | [jvm]<br>open operator override fun [next](next.md)(): [T](index.md) |

## Properties

| Name | Summary |
|---|---|
| [iterator](iterator.md) | [jvm]<br>val [iterator](iterator.md): [PosRangeIterator](../-pos-range-iterator/index.md)&lt;[POS](index.md)&gt; |
