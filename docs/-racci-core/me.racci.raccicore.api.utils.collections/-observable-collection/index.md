---
title: ObservableCollection
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[ObservableCollection](index.html)



# ObservableCollection



[jvm]\
interface [ObservableCollection](index.html)&lt;[T](index.html)&gt; : [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[T](index.html)&gt; , [ObservableHolder](../-observable-holder/index.html)



## Functions


| Name | Summary |
|---|---|
| [add](index.html#-336316080%2FFunctions%2F863300109) | [jvm]<br>abstract fun [add](index.html#-336316080%2FFunctions%2F863300109)(element: [T](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [addAll](index.html#1622835035%2FFunctions%2F863300109) | [jvm]<br>abstract fun [addAll](index.html#1622835035%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [clear](../-online-player-collection/index.html#1405312578%2FFunctions%2F863300109) | [jvm]<br>abstract fun [clear](../-online-player-collection/index.html#1405312578%2FFunctions%2F863300109)() |
| [contains](index.html#1825712522%2FFunctions%2F863300109) | [jvm]<br>abstract operator fun [contains](index.html#1825712522%2FFunctions%2F863300109)(element: [T](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.html#-348659435%2FFunctions%2F863300109) | [jvm]<br>abstract fun [containsAll](index.html#-348659435%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.html#1532301601%2FFunctions%2F863300109) | [jvm]<br>open fun [forEach](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.html#1532301601%2FFunctions%2F863300109)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [T](index.html)&gt;) |
| [isEmpty](../-online-player-collection/index.html#-719293276%2FFunctions%2F863300109) | [jvm]<br>abstract fun [isEmpty](../-online-player-collection/index.html#-719293276%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](../-online-player-collection/index.html#1177836957%2FFunctions%2F863300109) | [jvm]<br>abstract operator override fun [iterator](../-online-player-collection/index.html#1177836957%2FFunctions%2F863300109)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;[T](index.html)&gt; |
| [observe](../-observable-holder/observe.html) | [jvm]<br>open fun [observe](../-observable-holder/observe.html)(listener: [ObservableListener](../index.html#290302064%2FClasslikes%2F863300109)) |
| [parallelStream](../-online-player-collection/index.html#-1592339412%2FFunctions%2F863300109) | [jvm]<br>open fun [parallelStream](../-online-player-collection/index.html#-1592339412%2FFunctions%2F863300109)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[T](index.html)&gt; |
| [remove](index.html#-866564265%2FFunctions%2F863300109) | [jvm]<br>abstract fun [remove](index.html#-866564265%2FFunctions%2F863300109)(element: [T](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeAll](index.html#-1840690270%2FFunctions%2F863300109) | [jvm]<br>abstract fun [removeAll](index.html#-1840690270%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeIf](index.html#1655623621%2FFunctions%2F863300109) | [jvm]<br>open fun [removeIf](index.html#1655623621%2FFunctions%2F863300109)(p0: [Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html)&lt;in [T](index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [retainAll](index.html#-1279972125%2FFunctions%2F863300109) | [jvm]<br>abstract fun [retainAll](index.html#-1279972125%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [runListeners](../-observable-holder/run-listeners.html) | [jvm]<br>open fun [runListeners](../-observable-holder/run-listeners.html)(action: [ObservableAction](../-observable-action/index.html)) |
| [spliterator](../-online-player-collection/index.html#1956926474%2FFunctions%2F863300109) | [jvm]<br>open override fun [spliterator](../-online-player-collection/index.html#1956926474%2FFunctions%2F863300109)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;[T](index.html)&gt; |
| [stream](../-online-player-collection/index.html#135225651%2FFunctions%2F863300109) | [jvm]<br>open fun [stream](../-online-player-collection/index.html#135225651%2FFunctions%2F863300109)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[T](index.html)&gt; |
| [toArray](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109) | [jvm]<br>~~open~~ ~~fun~~ ~~&lt;~~[T](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)~~&gt;~~ [~~toArray~~](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109)~~(~~~~p0~~~~:~~ [IntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109)&gt;&gt;~~)~~~~:~~ [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109)&gt; |


## Properties


| Name | Summary |
|---|---|
| [listeners](../-observable-holder/listeners.html) | [jvm]<br>abstract val [listeners](../-observable-holder/listeners.html): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[ObservableListener](../index.html#290302064%2FClasslikes%2F863300109)&gt; |
| [size](../-online-player-collection/index.html#-113084078%2FProperties%2F863300109) | [jvm]<br>abstract val [size](../-online-player-collection/index.html#-113084078%2FProperties%2F863300109): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |


## Inheritors


| Name |
|---|
| [ObservableList](../-observable-list/index.html) |
| [ObservableSet](../-observable-set/index.html) |

