---
title: ObservableList
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[ObservableList](index.html)



# ObservableList



[jvm]\
class [ObservableList](index.html)&lt;[T](index.html)&gt;(list: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[T](index.html)&gt;) : [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[T](index.html)&gt; , [ObservableCollection](../-observable-collection/index.html)&lt;[T](index.html)&gt;



## Functions


| Name | Summary |
|---|---|
| [add](add.html) | [jvm]<br>open override fun [add](add.html)(element: [T](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>open override fun [add](add.html)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), element: [T](index.html)) |
| [addAll](add-all.html) | [jvm]<br>open override fun [addAll](add-all.html)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>open override fun [addAll](add-all.html)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [clear](clear.html) | [jvm]<br>open override fun [clear](clear.html)() |
| [contains](index.html#765883978%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [contains](index.html#765883978%2FFunctions%2F863300109)(element: [T](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.html#-225903147%2FFunctions%2F863300109) | [jvm]<br>open override fun [containsAll](index.html#-225903147%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.html#1532301601%2FFunctions%2F863300109) | [jvm]<br>open fun [forEach](../../me.racci.raccicore.api.utils.minecraft/-pos-range/index.html#1532301601%2FFunctions%2F863300109)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [T](index.html)&gt;) |
| [get](index.html#961975567%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [get](index.html#961975567%2FFunctions%2F863300109)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [T](index.html) |
| [indexOf](index.html#-407930336%2FFunctions%2F863300109) | [jvm]<br>open override fun [indexOf](index.html#-407930336%2FFunctions%2F863300109)(element: [T](index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isEmpty](index.html#-1000881820%2FFunctions%2F863300109) | [jvm]<br>open override fun [isEmpty](index.html#-1000881820%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](index.html#-1577986619%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [iterator](index.html#-1577986619%2FFunctions%2F863300109)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;[T](index.html)&gt; |
| [lastIndexOf](index.html#1327716778%2FFunctions%2F863300109) | [jvm]<br>open override fun [lastIndexOf](index.html#1327716778%2FFunctions%2F863300109)(element: [T](index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [listIterator](list-iterator.html) | [jvm]<br>open override fun [listIterator](list-iterator.html)(): [MutableListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list-iterator/index.html)&lt;[T](index.html)&gt;<br>open override fun [listIterator](list-iterator.html)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [MutableListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list-iterator/index.html)&lt;[T](index.html)&gt; |
| [observe](../-observable-holder/observe.html) | [jvm]<br>open fun [observe](../-observable-holder/observe.html)(listener: [ObservableListener](../index.html#290302064%2FClasslikes%2F863300109)) |
| [parallelStream](../-online-player-collection/index.html#-1592339412%2FFunctions%2F863300109) | [jvm]<br>open fun [parallelStream](../-online-player-collection/index.html#-1592339412%2FFunctions%2F863300109)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[T](index.html)&gt; |
| [remove](remove.html) | [jvm]<br>open override fun [remove](remove.html)(element: [T](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeAll](remove-all.html) | [jvm]<br>open override fun [removeAll](remove-all.html)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeAt](remove-at.html) | [jvm]<br>open override fun [removeAt](remove-at.html)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [T](index.html) |
| [removeIf](../-observable-collection/index.html#1655623621%2FFunctions%2F863300109) | [jvm]<br>open fun [removeIf](../-observable-collection/index.html#1655623621%2FFunctions%2F863300109)(p0: [Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html)&lt;in [T](index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replaceAll](index.html#901675341%2FFunctions%2F863300109) | [jvm]<br>open fun [replaceAll](index.html#901675341%2FFunctions%2F863300109)(p0: [UnaryOperator](https://docs.oracle.com/javase/8/docs/api/java/util/function/UnaryOperator.html)&lt;[T](index.html)&gt;) |
| [retainAll](retain-all.html) | [jvm]<br>open override fun [retainAll](retain-all.html)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [runListeners](../-observable-holder/run-listeners.html) | [jvm]<br>open fun [runListeners](../-observable-holder/run-listeners.html)(action: [ObservableAction](../-observable-action/index.html)) |
| [set](set.html) | [jvm]<br>open operator override fun [set](set.html)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), element: [T](index.html)): [T](index.html) |
| [sort](index.html#-1169394793%2FFunctions%2F863300109) | [jvm]<br>open fun [sort](index.html#-1169394793%2FFunctions%2F863300109)(p0: [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;in [T](index.html)&gt;) |
| [spliterator](index.html#703021258%2FFunctions%2F863300109) | [jvm]<br>open override fun [spliterator](index.html#703021258%2FFunctions%2F863300109)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;[T](index.html)&gt; |
| [stream](../-online-player-collection/index.html#135225651%2FFunctions%2F863300109) | [jvm]<br>open fun [stream](../-online-player-collection/index.html#135225651%2FFunctions%2F863300109)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[T](index.html)&gt; |
| [subList](index.html#-11148114%2FFunctions%2F863300109) | [jvm]<br>open override fun [subList](index.html#-11148114%2FFunctions%2F863300109)(fromIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), toIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[T](index.html)&gt; |
| [toArray](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109) | [jvm]<br>~~open~~ ~~fun~~ ~~&lt;~~[T](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)~~&gt;~~ [~~toArray~~](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109)~~(~~~~p0~~~~:~~ [IntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109)&gt;&gt;~~)~~~~:~~ [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109)&gt; |


## Properties


| Name | Summary |
|---|---|
| [listeners](listeners.html) | [jvm]<br>open override val [listeners](listeners.html): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[ObservableListener](../index.html#290302064%2FClasslikes%2F863300109)&gt; |
| [size](index.html#844915858%2FProperties%2F863300109) | [jvm]<br>open override val [size](index.html#844915858%2FProperties%2F863300109): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

