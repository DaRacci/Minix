//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.collections](../index.md)/[ObservableCollection](index.md)

# ObservableCollection

[jvm]\
interface [ObservableCollection](index.md)&lt;[T](index.md)&gt; : [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[T](index.md)&gt; , [ObservableHolder](../-observable-holder/index.md)

## Functions

| Name | Summary |
|---|---|
| [add](index.md#-336316080%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [add](index.md#-336316080%2FFunctions%2F-519281799)(element: [T](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [addAll](index.md#1622835035%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [addAll](index.md#1622835035%2FFunctions%2F-519281799)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [clear](../-online-player-collection/index.md#1405312578%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [clear](../-online-player-collection/index.md#1405312578%2FFunctions%2F-519281799)() |
| [contains](index.md#1825712522%2FFunctions%2F-519281799) | [jvm]<br>abstract operator fun [contains](index.md#1825712522%2FFunctions%2F-519281799)(element: [T](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.md#-348659435%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [containsAll](index.md#-348659435%2FFunctions%2F-519281799)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](index.md#1532301601%2FFunctions%2F-519281799) | [jvm]<br>open fun [forEach](index.md#1532301601%2FFunctions%2F-519281799)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [T](index.md)&gt;) |
| [isEmpty](../-online-player-collection/index.md#-719293276%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [isEmpty](../-online-player-collection/index.md#-719293276%2FFunctions%2F-519281799)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](../-online-player-collection/index.md#1177836957%2FFunctions%2F-519281799) | [jvm]<br>abstract operator override fun [iterator](../-online-player-collection/index.md#1177836957%2FFunctions%2F-519281799)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;[T](index.md)&gt; |
| [observe](../-observable-holder/observe.md) | [jvm]<br>open fun [observe](../-observable-holder/observe.md)(listener: [ObservableListener](../index.md#1056729540%2FClasslikes%2F-519281799)) |
| [parallelStream](../-online-player-collection/index.md#-1592339412%2FFunctions%2F-519281799) | [jvm]<br>open fun [parallelStream](../-online-player-collection/index.md#-1592339412%2FFunctions%2F-519281799)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[T](index.md)&gt; |
| [remove](index.md#-866564265%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [remove](index.md#-866564265%2FFunctions%2F-519281799)(element: [T](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeAll](index.md#-1840690270%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [removeAll](index.md#-1840690270%2FFunctions%2F-519281799)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeIf](index.md#1655623621%2FFunctions%2F-519281799) | [jvm]<br>open fun [removeIf](index.md#1655623621%2FFunctions%2F-519281799)(p0: [Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html)&lt;in [T](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [retainAll](index.md#-1279972125%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [retainAll](index.md#-1279972125%2FFunctions%2F-519281799)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [runListeners](../-observable-holder/run-listeners.md) | [jvm]<br>open fun [runListeners](../-observable-holder/run-listeners.md)(action: [ObservableAction](../-observable-action/index.md)) |
| [spliterator](../-online-player-collection/index.md#1956926474%2FFunctions%2F-519281799) | [jvm]<br>open override fun [spliterator](../-online-player-collection/index.md#1956926474%2FFunctions%2F-519281799)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;[T](index.md)&gt; |
| [stream](../-online-player-collection/index.md#135225651%2FFunctions%2F-519281799) | [jvm]<br>open fun [stream](../-online-player-collection/index.md#135225651%2FFunctions%2F-519281799)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[T](index.md)&gt; |
| [toArray](../-online-player-collection/index.md#-1215154575%2FFunctions%2F-519281799) | [jvm]<br>~~open~~ ~~fun~~ ~~&lt;~~[T](../-online-player-collection/index.md#-1215154575%2FFunctions%2F-519281799) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)~~&gt;~~ [~~toArray~~](../-online-player-collection/index.md#-1215154575%2FFunctions%2F-519281799)~~(~~~~p0~~~~:~~ [IntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-online-player-collection/index.md#-1215154575%2FFunctions%2F-519281799)&gt;&gt;~~)~~~~:~~ [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-online-player-collection/index.md#-1215154575%2FFunctions%2F-519281799)&gt; |

## Properties

| Name | Summary |
|---|---|
| [listeners](../-observable-holder/listeners.md) | [jvm]<br>abstract val [listeners](../-observable-holder/listeners.md): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[ObservableListener](../index.md#1056729540%2FClasslikes%2F-519281799)&gt; |
| [size](../-online-player-collection/index.md#-113084078%2FProperties%2F-519281799) | [jvm]<br>abstract val [size](../-online-player-collection/index.md#-113084078%2FProperties%2F-519281799): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Inheritors

| Name |
|---|
| [ObservableList](../-observable-list/index.md) |
| [ObservableSet](../-observable-set/index.md) |
