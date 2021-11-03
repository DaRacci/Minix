//[RacciCore](../../../index.md)/[me.racci.raccicore.utils](../index.md)/[PosRange](index.md)

# PosRange

[jvm]\
class [PosRange](index.md)&lt;[T](index.md), [POS](index.md) : [VectorComparable](../-vector-comparable/index.md)&lt;[POS](index.md)&gt;&gt;(first: [POS](index.md), last: [POS](index.md), buildIterator: () -&gt; [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[T](index.md)&gt;) : [ClosedRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-closed-range/index.html)&lt;[POS](index.md)&gt; , [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[T](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [contains](contains.md) | [jvm]<br>open operator override fun [contains](contains.md)(value: [POS](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](../../me.racci.raccicore.utils.collections/-observable-collection/index.md#1532301601%2FFunctions%2F-519281799) | [jvm]<br>open fun [forEach](../../me.racci.raccicore.utils.collections/-observable-collection/index.md#1532301601%2FFunctions%2F-519281799)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [T](index.md)&gt;) |
| [isEmpty](index.md#1458256964%2FFunctions%2F-519281799) | [jvm]<br>open fun [isEmpty](index.md#1458256964%2FFunctions%2F-519281799)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](iterator.md) | [jvm]<br>open operator override fun [iterator](iterator.md)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[T](index.md)&gt; |
| [spliterator](../../me.racci.raccicore.utils.collections/-expiration-list-impl/index.md#-1387152138%2FFunctions%2F-519281799) | [jvm]<br>open fun [spliterator](../../me.racci.raccicore.utils.collections/-expiration-list-impl/index.md#-1387152138%2FFunctions%2F-519281799)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;[T](index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [buildIterator](build-iterator.md) | [jvm]<br>val [buildIterator](build-iterator.md): () -&gt; [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[T](index.md)&gt; |
| [endInclusive](end-inclusive.md) | [jvm]<br>open override val [endInclusive](end-inclusive.md): [POS](index.md) |
| [first](first.md) | [jvm]<br>val [first](first.md): [POS](index.md) |
| [last](last.md) | [jvm]<br>val [last](last.md): [POS](index.md) |
| [start](start.md) | [jvm]<br>open override val [start](start.md): [POS](index.md) |

## Extensions

| Name | Summary |
|---|---|
| [contains](../contains.md) | [jvm]<br>operator fun [PosRange](index.md)&lt;*, [BlockPos](../-block-pos/index.md)&gt;.[contains](../contains.md)(other: Location): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>operator fun [PosRange](index.md)&lt;*, [BlockPos](../-block-pos/index.md)&gt;.[contains](../contains.md)(other: Block): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>operator fun [PosRange](index.md)&lt;*, [ChunkPos](../-chunk-pos/index.md)&gt;.[contains](../contains.md)(other: Chunk): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
