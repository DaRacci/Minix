---
title: PosRange
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.minecraft](../index.html)/[PosRange](index.html)



# PosRange



[jvm]\
class [PosRange](index.html)&lt;[T](index.html), [POS](index.html) : [VectorComparable](../-vector-comparable/index.html)&lt;[POS](index.html)&gt;&gt;(first: [POS](index.html), last: [POS](index.html), buildIterator: () -&gt; [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[T](index.html)&gt;) : [ClosedRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-closed-range/index.html)&lt;[POS](index.html)&gt; , [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)&lt;[T](index.html)&gt;



## Functions


| Name | Summary |
|---|---|
| [contains](contains.html) | [jvm]<br>open operator override fun [contains](contains.html)(value: [POS](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](index.html#1532301601%2FFunctions%2F863300109) | [jvm]<br>open fun [forEach](index.html#1532301601%2FFunctions%2F863300109)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [T](index.html)&gt;) |
| [isEmpty](index.html#1458256964%2FFunctions%2F863300109) | [jvm]<br>open fun [isEmpty](index.html#1458256964%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](iterator.html) | [jvm]<br>open operator override fun [iterator](iterator.html)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[T](index.html)&gt; |
| [spliterator](index.html#-1387152138%2FFunctions%2F863300109) | [jvm]<br>open fun [spliterator](index.html#-1387152138%2FFunctions%2F863300109)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;[T](index.html)&gt; |


## Properties


| Name | Summary |
|---|---|
| [buildIterator](build-iterator.html) | [jvm]<br>val [buildIterator](build-iterator.html): () -&gt; [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[T](index.html)&gt; |
| [endInclusive](end-inclusive.html) | [jvm]<br>open override val [endInclusive](end-inclusive.html): [POS](index.html) |
| [first](first.html) | [jvm]<br>val [first](first.html): [POS](index.html) |
| [last](last.html) | [jvm]<br>val [last](last.html): [POS](index.html) |
| [start](start.html) | [jvm]<br>open override val [start](start.html): [POS](index.html) |


## Extensions


| Name | Summary |
|---|---|
| [contains](../contains.html) | [jvm]<br>operator fun [PosRange](index.html)&lt;*, [BlockPos](../-block-pos/index.html)&gt;.[contains](../contains.html)(other: Location): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>operator fun [PosRange](index.html)&lt;*, [BlockPos](../-block-pos/index.html)&gt;.[contains](../contains.html)(other: Block): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>operator fun [PosRange](index.html)&lt;*, [ChunkPos](../-chunk-pos/index.html)&gt;.[contains](../contains.html)(other: Chunk): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

