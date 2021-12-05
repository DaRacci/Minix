---
title: OnlinePlayerSet
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[OnlinePlayerSet](index.html)



# OnlinePlayerSet



[jvm]\
class [OnlinePlayerSet](index.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)) : [HashSet](https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html)&lt;Player&gt; , [OnlinePlayerCollection](../-online-player-collection/index.html)



## Functions


| Name | Summary |
|---|---|
| [add](add.html) | [jvm]<br>open override fun [add](add.html)(element: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>[jvm]<br>open override fun [add](add.html)(player: Player, whenPlayerQuit: [WhenPlayerQuitCollectionCallback](../index.html#-1583039622%2FClasslikes%2F863300109)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Adds a new Player to the collection with a callback for when the player quits the server. |
| [addAll](index.html#-2053129901%2FFunctions%2F863300109) | [jvm]<br>open override fun [addAll](index.html#-2053129901%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [checkRegistration](../-online-player-collection/check-registration.html) | [jvm]<br>open fun [checkRegistration](../-online-player-collection/check-registration.html)() |
| [clear](index.html#2005357580%2FFunctions%2F863300109) | [jvm]<br>open override fun [clear](index.html#2005357580%2FFunctions%2F863300109)() |
| [clearQuiting](../-online-player-collection/clear-quiting.html) | [jvm]<br>open fun [clearQuiting](../-online-player-collection/clear-quiting.html)()<br>Clear the collection calling all [WhenPlayerQuitCollectionCallback](../index.html#-1583039622%2FClasslikes%2F863300109) from the Players. |
| [clone](index.html#-1329185028%2FFunctions%2F863300109) | [jvm]<br>open override fun [clone](index.html#-1329185028%2FFunctions%2F863300109)(): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [contains](index.html#1998718364%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [contains](index.html#1998718364%2FFunctions%2F863300109)(element: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.html#974630905%2FFunctions%2F863300109) | [jvm]<br>open override fun [containsAll](index.html#974630905%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [equals](index.html#-613647372%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [equals](index.html#-613647372%2FFunctions%2F863300109)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](../-online-player-collection/index.html#-1461011823%2FFunctions%2F863300109) | [jvm]<br>open fun [forEach](../-online-player-collection/index.html#-1461011823%2FFunctions%2F863300109)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in Player&gt;) |
| [hashCode](index.html#114469490%2FFunctions%2F863300109) | [jvm]<br>open override fun [hashCode](index.html#114469490%2FFunctions%2F863300109)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isEmpty](index.html#1591118294%2FFunctions%2F863300109) | [jvm]<br>open override fun [isEmpty](index.html#1591118294%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](index.html#1464605587%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [iterator](index.html#1464605587%2FFunctions%2F863300109)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;Player&gt; |
| [parallelStream](../-online-player-collection/index.html#-1592339412%2FFunctions%2F863300109) | [jvm]<br>open fun [parallelStream](../-online-player-collection/index.html#-1592339412%2FFunctions%2F863300109)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;Player&gt; |
| [quit](quit.html) | [jvm]<br>open override fun [quit](quit.html)(player: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Removes the player from the collection, calling the [WhenPlayerQuitCollectionCallback](../index.html#-1583039622%2FClasslikes%2F863300109) provided. |
| [remove](remove.html) | [jvm]<br>open override fun [remove](remove.html)(element: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeAll](index.html#1603351826%2FFunctions%2F863300109) | [jvm]<br>open override fun [removeAll](index.html#1603351826%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeIf](../-online-player-collection/index.html#-1875219347%2FFunctions%2F863300109) | [jvm]<br>open fun [removeIf](../-online-player-collection/index.html#-1875219347%2FFunctions%2F863300109)(p0: [Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html)&lt;in Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [retainAll](index.html#1683639251%2FFunctions%2F863300109) | [jvm]<br>open override fun [retainAll](index.html#1683639251%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [spliterator](index.html#1577615420%2FFunctions%2F863300109) | [jvm]<br>open override fun [spliterator](index.html#1577615420%2FFunctions%2F863300109)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;Player&gt; |
| [stream](../-online-player-collection/index.html#135225651%2FFunctions%2F863300109) | [jvm]<br>open fun [stream](../-online-player-collection/index.html#135225651%2FFunctions%2F863300109)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;Player&gt; |
| [toArray](index.html#1371586523%2FFunctions%2F863300109) | [jvm]<br>open override fun [toArray](index.html#1371586523%2FFunctions%2F863300109)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;<br>open override fun &lt;[T](index.html#-1117581088%2FFunctions%2F863300109) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [toArray](index.html#-1117581088%2FFunctions%2F863300109)(p0: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.html#-1117581088%2FFunctions%2F863300109)&gt;): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.html#-1117581088%2FFunctions%2F863300109)&gt;<br>~~open~~ ~~fun~~ ~~&lt;~~[T](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)~~&gt;~~ [~~toArray~~](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109)~~(~~~~p0~~~~:~~ [IntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109)&gt;&gt;~~)~~~~:~~ [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109)&gt; |
| [toString](index.html#-42557405%2FFunctions%2F863300109) | [jvm]<br>open override fun [toString](index.html#-42557405%2FFunctions%2F863300109)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |


## Properties


| Name | Summary |
|---|---|
| [plugin](plugin.html) | [jvm]<br>open override val [plugin](plugin.html): [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) |
| [size](index.html#1013825888%2FProperties%2F863300109) | [jvm]<br>open override val [size](index.html#1013825888%2FProperties%2F863300109): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

