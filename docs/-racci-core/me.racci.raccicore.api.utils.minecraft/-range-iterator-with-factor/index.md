---
title: RangeIteratorWithFactor
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.minecraft](../index.html)/[RangeIteratorWithFactor](index.html)



# RangeIteratorWithFactor



[jvm]\
class [RangeIteratorWithFactor](index.html)&lt;[T](index.html), [POS](index.html) : [VectorComparable](../-vector-comparable/index.html)&lt;[POS](index.html)&gt;&gt;(start: [T](index.html), end: [T](index.html), factor: ([POS](index.html)) -&gt; [T](index.html), posFactor: ([T](index.html)) -&gt; [POS](index.html)) : [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[T](index.html)&gt;



## Functions


| Name | Summary |
|---|---|
| [forEachRemaining](index.html#-511368593%2FFunctions%2F863300109) | [jvm]<br>open fun [forEachRemaining](index.html#-511368593%2FFunctions%2F863300109)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [T](index.html)&gt;) |
| [hasNext](has-next.html) | [jvm]<br>open operator override fun [hasNext](has-next.html)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [next](next.html) | [jvm]<br>open operator override fun [next](next.html)(): [T](index.html) |


## Properties


| Name | Summary |
|---|---|
| [iterator](iterator.html) | [jvm]<br>val [iterator](iterator.html): [PosRangeIterator](../-pos-range-iterator/index.html)&lt;[POS](index.html)&gt; |

