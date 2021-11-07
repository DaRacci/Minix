//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.collections](../index.md)/[ObservableSet](index.md)

# ObservableSet

[jvm]\
class [ObservableSet](index.md)&lt;[T](index.md)&gt;(set: [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[T](index.md)&gt;) : [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[T](index.md)&gt; , [ObservableCollection](../-observable-collection/index.md)&lt;[T](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [add](add.md) | [jvm]<br>open override fun [add](add.md)(element: [T](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [addAll](add-all.md) | [jvm]<br>open override fun [addAll](add-all.md)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [clear](clear.md) | [jvm]<br>open override fun [clear](clear.md)() |
| [contains](index.md#607101300%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [contains](index.md#607101300%2FFunctions%2F-1216412040)(element: [T](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.md#1318510207%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [containsAll](index.md#1318510207%2FFunctions%2F-1216412040)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](../-observable-collection/index.md#1532301601%2FFunctions%2F-1216412040) | [jvm]<br>open fun [forEach](../-observable-collection/index.md#1532301601%2FFunctions%2F-1216412040)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [T](index.md)&gt;) |
| [isEmpty](index.md#-477621106%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [isEmpty](index.md#-477621106%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](iterator.md) | [jvm]<br>open operator override fun [iterator](iterator.md)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;[T](index.md)&gt; |
| [observe](../-observable-holder/observe.md) | [jvm]<br>open fun [observe](../-observable-holder/observe.md)(listener: [ObservableListener](../index.md#1056729540%2FClasslikes%2F-1216412040)) |
| [parallelStream](../-online-player-collection/index.md#-1592339412%2FFunctions%2F-1216412040) | [jvm]<br>open fun [parallelStream](../-online-player-collection/index.md#-1592339412%2FFunctions%2F-1216412040)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[T](index.md)&gt; |
| [remove](remove.md) | [jvm]<br>open override fun [remove](remove.md)(element: [T](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeAll](remove-all.md) | [jvm]<br>open override fun [removeAll](remove-all.md)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeIf](../-observable-collection/index.md#1655623621%2FFunctions%2F-1216412040) | [jvm]<br>open fun [removeIf](../-observable-collection/index.md#1655623621%2FFunctions%2F-1216412040)(p0: [Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html)&lt;in [T](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [retainAll](retain-all.md) | [jvm]<br>open override fun [retainAll](retain-all.md)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [runListeners](../-observable-holder/run-listeners.md) | [jvm]<br>open fun [runListeners](../-observable-holder/run-listeners.md)(action: [ObservableAction](../-observable-action/index.md)) |
| [spliterator](index.md#-989466892%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [spliterator](index.md#-989466892%2FFunctions%2F-1216412040)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;[T](index.md)&gt; |
| [stream](../-online-player-collection/index.md#135225651%2FFunctions%2F-1216412040) | [jvm]<br>open fun [stream](../-online-player-collection/index.md#135225651%2FFunctions%2F-1216412040)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[T](index.md)&gt; |
| [toArray](../-online-player-collection/index.md#-1215154575%2FFunctions%2F-1216412040) | [jvm]<br>~~open~~ ~~fun~~ ~~&lt;~~[T](../-online-player-collection/index.md#-1215154575%2FFunctions%2F-1216412040) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)~~&gt;~~ [~~toArray~~](../-online-player-collection/index.md#-1215154575%2FFunctions%2F-1216412040)~~(~~~~p0~~~~:~~ [IntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-online-player-collection/index.md#-1215154575%2FFunctions%2F-1216412040)&gt;&gt;~~)~~~~:~~ [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-online-player-collection/index.md#-1215154575%2FFunctions%2F-1216412040)&gt; |

## Properties

| Name | Summary |
|---|---|
| [listeners](listeners.md) | [jvm]<br>open override val [listeners](listeners.md): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[ObservableListener](../index.md#1056729540%2FClasslikes%2F-1216412040)&gt; |
| [size](index.md#1578037672%2FProperties%2F-1216412040) | [jvm]<br>open override val [size](index.md#1578037672%2FProperties%2F-1216412040): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
