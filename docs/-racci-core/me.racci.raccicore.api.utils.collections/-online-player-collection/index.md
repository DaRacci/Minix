---
title: OnlinePlayerCollection
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[OnlinePlayerCollection](index.html)



# OnlinePlayerCollection



[jvm]\
interface [OnlinePlayerCollection](index.html) : [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;Player&gt; , [KListener](../../me.racci.raccicore.api.extensions/-k-listener/index.html)&lt;[RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)&gt;



## Functions


| Name | Summary |
|---|---|
| [add](index.html#-1363033720%2FFunctions%2F863300109) | [jvm]<br>abstract fun [add](index.html#-1363033720%2FFunctions%2F863300109)(element: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>[jvm]<br>open fun [add](add.html)(player: Player, whenPlayerQuit: [WhenPlayerQuitCollectionCallback](../index.html#-1583039622%2FClasslikes%2F863300109)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Adds a new Player to the collection with a callback for when the player quits the server. |
| [addAll](index.html#1674017175%2FFunctions%2F863300109) | [jvm]<br>abstract fun [addAll](index.html#1674017175%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [checkRegistration](check-registration.html) | [jvm]<br>open fun [checkRegistration](check-registration.html)() |
| [clear](index.html#1405312578%2FFunctions%2F863300109) | [jvm]<br>abstract fun [clear](index.html#1405312578%2FFunctions%2F863300109)() |
| [clearQuiting](clear-quiting.html) | [jvm]<br>open fun [clearQuiting](clear-quiting.html)()<br>Clear the collection calling all [WhenPlayerQuitCollectionCallback](../index.html#-1583039622%2FClasslikes%2F863300109) from the Players. |
| [contains](index.html#-1747698034%2FFunctions%2F863300109) | [jvm]<br>abstract operator fun [contains](index.html#-1747698034%2FFunctions%2F863300109)(element: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.html#-955304675%2FFunctions%2F863300109) | [jvm]<br>abstract fun [containsAll](index.html#-955304675%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](index.html#-1461011823%2FFunctions%2F863300109) | [jvm]<br>open fun [forEach](index.html#-1461011823%2FFunctions%2F863300109)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in Player&gt;) |
| [isEmpty](index.html#-719293276%2FFunctions%2F863300109) | [jvm]<br>abstract fun [isEmpty](index.html#-719293276%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](index.html#1177836957%2FFunctions%2F863300109) | [jvm]<br>abstract operator override fun [iterator](index.html#1177836957%2FFunctions%2F863300109)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;Player&gt; |
| [parallelStream](index.html#-1592339412%2FFunctions%2F863300109) | [jvm]<br>open fun [parallelStream](index.html#-1592339412%2FFunctions%2F863300109)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;Player&gt; |
| [quit](quit.html) | [jvm]<br>open fun [quit](quit.html)(player: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Removes the player from the collection, calling the [WhenPlayerQuitCollectionCallback](../index.html#-1583039622%2FClasslikes%2F863300109) provided. |
| [remove](index.html#-1832428191%2FFunctions%2F863300109) | [jvm]<br>abstract fun [remove](index.html#-1832428191%2FFunctions%2F863300109)(element: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeAll](index.html#1885396784%2FFunctions%2F863300109) | [jvm]<br>abstract fun [removeAll](index.html#1885396784%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeIf](index.html#-1875219347%2FFunctions%2F863300109) | [jvm]<br>open fun [removeIf](index.html#-1875219347%2FFunctions%2F863300109)(p0: [Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html)&lt;in Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [retainAll](index.html#-667323121%2FFunctions%2F863300109) | [jvm]<br>abstract fun [retainAll](index.html#-667323121%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [spliterator](index.html#1956926474%2FFunctions%2F863300109) | [jvm]<br>open override fun [spliterator](index.html#1956926474%2FFunctions%2F863300109)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;Player&gt; |
| [stream](index.html#135225651%2FFunctions%2F863300109) | [jvm]<br>open fun [stream](index.html#135225651%2FFunctions%2F863300109)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;Player&gt; |
| [toArray](index.html#-1215154575%2FFunctions%2F863300109) | [jvm]<br>~~open~~ ~~fun~~ ~~&lt;~~[T](index.html#-1215154575%2FFunctions%2F863300109) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)~~&gt;~~ [~~toArray~~](index.html#-1215154575%2FFunctions%2F863300109)~~(~~~~p0~~~~:~~ [IntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.html#-1215154575%2FFunctions%2F863300109)&gt;&gt;~~)~~~~:~~ [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.html#-1215154575%2FFunctions%2F863300109)&gt; |


## Properties


| Name | Summary |
|---|---|
| [plugin](../../me.racci.raccicore.api.extensions/-with-plugin/plugin.html) | [jvm]<br>abstract val [plugin](../../me.racci.raccicore.api.extensions/-with-plugin/plugin.html): [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) |
| [size](index.html#-113084078%2FProperties%2F863300109) | [jvm]<br>abstract val [size](index.html#-113084078%2FProperties%2F863300109): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |


## Inheritors


| Name |
|---|
| [OnlinePlayerList](../-online-player-list/index.html) |
| [OnlinePlayerSet](../-online-player-set/index.html) |

