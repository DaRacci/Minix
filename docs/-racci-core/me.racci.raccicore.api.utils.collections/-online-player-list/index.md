---
title: OnlinePlayerList
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[OnlinePlayerList](index.html)



# OnlinePlayerList



[jvm]\
class [OnlinePlayerList](index.html)(plugin: [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)) : [LinkedList](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html)&lt;Player&gt; , [OnlinePlayerCollection](../-online-player-collection/index.html)



## Functions


| Name | Summary |
|---|---|
| [add](add.html) | [jvm]<br>open override fun [add](add.html)(element: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>open override fun [add](index.html#1188716360%2FFunctions%2F863300109)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), element: Player)<br>[jvm]<br>open override fun [add](add.html)(player: Player, whenPlayerQuit: Player.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Adds a new Player to the collection with a callback for when the player quits the server. |
| [addAll](index.html#-550232964%2FFunctions%2F863300109) | [jvm]<br>open override fun [addAll](index.html#-550232964%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>open override fun [addAll](index.html#1448244871%2FFunctions%2F863300109)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [addFirst](index.html#-1484477903%2FFunctions%2F863300109) | [jvm]<br>open override fun [addFirst](index.html#-1484477903%2FFunctions%2F863300109)(p0: Player) |
| [addLast](index.html#-1832360265%2FFunctions%2F863300109) | [jvm]<br>open override fun [addLast](index.html#-1832360265%2FFunctions%2F863300109)(p0: Player) |
| [checkRegistration](../-online-player-collection/check-registration.html) | [jvm]<br>open fun [checkRegistration](../-online-player-collection/check-registration.html)() |
| [clear](index.html#763819047%2FFunctions%2F863300109) | [jvm]<br>open override fun [clear](index.html#763819047%2FFunctions%2F863300109)() |
| [clearQuiting](../-online-player-collection/clear-quiting.html) | [jvm]<br>open fun [clearQuiting](../-online-player-collection/clear-quiting.html)()<br>Clear the collection calling all [WhenPlayerQuitCollectionCallback](../index.html#-1583039622%2FClasslikes%2F863300109) from the Players. |
| [clone](index.html#1724243735%2FFunctions%2F863300109) | [jvm]<br>open override fun [clone](index.html#1724243735%2FFunctions%2F863300109)(): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html) |
| [contains](index.html#74067105%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [contains](index.html#74067105%2FFunctions%2F863300109)(element: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](../-online-player-set/index.html#974630905%2FFunctions%2F863300109) | [jvm]<br>open override fun [containsAll](../-online-player-set/index.html#974630905%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [descendingIterator](index.html#-2000945600%2FFunctions%2F863300109) | [jvm]<br>open override fun [descendingIterator](index.html#-2000945600%2FFunctions%2F863300109)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;Player&gt; |
| [element](index.html#1002779800%2FFunctions%2F863300109) | [jvm]<br>open override fun [element](index.html#1002779800%2FFunctions%2F863300109)(): Player |
| [equals](index.html#450163474%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [equals](index.html#450163474%2FFunctions%2F863300109)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](../-online-player-collection/index.html#-1461011823%2FFunctions%2F863300109) | [jvm]<br>open fun [forEach](../-online-player-collection/index.html#-1461011823%2FFunctions%2F863300109)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in Player&gt;) |
| [get](index.html#-1715661540%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [get](index.html#-1715661540%2FFunctions%2F863300109)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Player |
| [getFirst](index.html#754697868%2FFunctions%2F863300109) | [jvm]<br>open override fun [getFirst](index.html#754697868%2FFunctions%2F863300109)(): Player |
| [getLast](index.html#-873788792%2FFunctions%2F863300109) | [jvm]<br>open override fun [getLast](index.html#-873788792%2FFunctions%2F863300109)(): Player |
| [hashCode](index.html#1188567444%2FFunctions%2F863300109) | [jvm]<br>open override fun [hashCode](index.html#1188567444%2FFunctions%2F863300109)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [indexOf](index.html#1814053253%2FFunctions%2F863300109) | [jvm]<br>open override fun [indexOf](index.html#1814053253%2FFunctions%2F863300109)(element: Player): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isEmpty](index.html#243630920%2FFunctions%2F863300109) | [jvm]<br>open override fun [isEmpty](index.html#243630920%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](index.html#1869147840%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [iterator](index.html#1869147840%2FFunctions%2F863300109)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;Player&gt; |
| [lastIndexOf](index.html#557202107%2FFunctions%2F863300109) | [jvm]<br>open override fun [lastIndexOf](index.html#557202107%2FFunctions%2F863300109)(element: Player): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [linkBefore](index.html#-351191147%2FFunctions%2F863300109) | [jvm]<br>open fun [linkBefore](index.html#-351191147%2FFunctions%2F863300109)(p0: Player, p1: [LinkedList.Node](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.Node.html)&lt;Player&gt;) |
| [linkLast](index.html#-1557335760%2FFunctions%2F863300109) | [jvm]<br>open fun [linkLast](index.html#-1557335760%2FFunctions%2F863300109)(p0: Player) |
| [listIterator](index.html#1041822371%2FFunctions%2F863300109) | [jvm]<br>open override fun [listIterator](index.html#1041822371%2FFunctions%2F863300109)(): [MutableListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list-iterator/index.html)&lt;Player&gt;<br>open override fun [listIterator](index.html#84103480%2FFunctions%2F863300109)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [MutableListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list-iterator/index.html)&lt;Player&gt; |
| [node](index.html#1168866210%2FFunctions%2F863300109) | [jvm]<br>open fun [node](index.html#1168866210%2FFunctions%2F863300109)(p0: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [LinkedList.Node](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.Node.html)&lt;Player&gt; |
| [offer](index.html#-1682297966%2FFunctions%2F863300109) | [jvm]<br>open override fun [offer](index.html#-1682297966%2FFunctions%2F863300109)(p0: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [offerFirst](index.html#-810806420%2FFunctions%2F863300109) | [jvm]<br>open override fun [offerFirst](index.html#-810806420%2FFunctions%2F863300109)(p0: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [offerLast](index.html#1237412380%2FFunctions%2F863300109) | [jvm]<br>open override fun [offerLast](index.html#1237412380%2FFunctions%2F863300109)(p0: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [parallelStream](../-online-player-collection/index.html#-1592339412%2FFunctions%2F863300109) | [jvm]<br>open fun [parallelStream](../-online-player-collection/index.html#-1592339412%2FFunctions%2F863300109)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;Player&gt; |
| [peek](index.html#410646987%2FFunctions%2F863300109) | [jvm]<br>open override fun [peek](index.html#410646987%2FFunctions%2F863300109)(): Player |
| [peekFirst](index.html#1293157855%2FFunctions%2F863300109) | [jvm]<br>open override fun [peekFirst](index.html#1293157855%2FFunctions%2F863300109)(): Player |
| [peekLast](index.html#-856419115%2FFunctions%2F863300109) | [jvm]<br>open override fun [peekLast](index.html#-856419115%2FFunctions%2F863300109)(): Player |
| [poll](index.html#330632167%2FFunctions%2F863300109) | [jvm]<br>open override fun [poll](index.html#330632167%2FFunctions%2F863300109)(): Player |
| [pollFirst](index.html#96200003%2FFunctions%2F863300109) | [jvm]<br>open override fun [pollFirst](index.html#96200003%2FFunctions%2F863300109)(): Player |
| [pollLast](index.html#-1310672655%2FFunctions%2F863300109) | [jvm]<br>open override fun [pollLast](index.html#-1310672655%2FFunctions%2F863300109)(): Player |
| [pop](index.html#922508899%2FFunctions%2F863300109) | [jvm]<br>open override fun [pop](index.html#922508899%2FFunctions%2F863300109)(): Player |
| [push](index.html#-765918842%2FFunctions%2F863300109) | [jvm]<br>open override fun [push](index.html#-765918842%2FFunctions%2F863300109)(p0: Player) |
| [quit](quit.html) | [jvm]<br>open override fun [quit](quit.html)(player: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Removes the player from the collection, calling the [WhenPlayerQuitCollectionCallback](../index.html#-1583039622%2FClasslikes%2F863300109) provided. |
| [remove](index.html#1458373762%2FFunctions%2F863300109) | [jvm]<br>open override fun [remove](index.html#1458373762%2FFunctions%2F863300109)(): Player<br>open override fun [remove](remove.html)(element: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeAll](index.html#-58608140%2FFunctions%2F863300109) | [jvm]<br>open override fun [removeAll](index.html#-58608140%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeAt](remove-at.html) | [jvm]<br>open override fun [removeAt](remove-at.html)(p0: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Player |
| [removeFirst](remove-first.html) | [jvm]<br>open override fun [removeFirst](remove-first.html)(): Player |
| [removeFirstOccurrence](index.html#1663750456%2FFunctions%2F863300109) | [jvm]<br>open override fun [removeFirstOccurrence](index.html#1663750456%2FFunctions%2F863300109)(p0: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeIf](../-online-player-collection/index.html#-1875219347%2FFunctions%2F863300109) | [jvm]<br>open fun [removeIf](../-online-player-collection/index.html#-1875219347%2FFunctions%2F863300109)(p0: [Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html)&lt;in Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeLast](remove-last.html) | [jvm]<br>open override fun [removeLast](remove-last.html)(): Player |
| [removeLastOccurrence](remove-last-occurrence.html) | [jvm]<br>open override fun [removeLastOccurrence](remove-last-occurrence.html)(p0: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeRange](index.html#-1757716403%2FFunctions%2F863300109) | [jvm]<br>open fun [removeRange](index.html#-1757716403%2FFunctions%2F863300109)(p0: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [replaceAll](index.html#1811606501%2FFunctions%2F863300109) | [jvm]<br>open fun [replaceAll](index.html#1811606501%2FFunctions%2F863300109)(p0: [UnaryOperator](https://docs.oracle.com/javase/8/docs/api/java/util/function/UnaryOperator.html)&lt;Player&gt;) |
| [retainAll](../-online-player-set/index.html#1683639251%2FFunctions%2F863300109) | [jvm]<br>open override fun [retainAll](../-online-player-set/index.html#1683639251%2FFunctions%2F863300109)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [set](index.html#-1568005239%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [set](index.html#-1568005239%2FFunctions%2F863300109)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), element: Player): Player |
| [sort](index.html#-1706192677%2FFunctions%2F863300109) | [jvm]<br>open fun [sort](index.html#-1706192677%2FFunctions%2F863300109)(p0: [Comparator](https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html)&lt;in Player&gt;) |
| [spliterator](index.html#-770154473%2FFunctions%2F863300109) | [jvm]<br>open override fun [spliterator](index.html#-770154473%2FFunctions%2F863300109)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;Player&gt; |
| [stream](../-online-player-collection/index.html#135225651%2FFunctions%2F863300109) | [jvm]<br>open fun [stream](../-online-player-collection/index.html#135225651%2FFunctions%2F863300109)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;Player&gt; |
| [subList](index.html#-993283982%2FFunctions%2F863300109) | [jvm]<br>open override fun [subList](index.html#-993283982%2FFunctions%2F863300109)(fromIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), toIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;Player&gt; |
| [toArray](index.html#-2041002698%2FFunctions%2F863300109) | [jvm]<br>open override fun [toArray](index.html#-2041002698%2FFunctions%2F863300109)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;<br>open override fun &lt;[T](index.html#-1997751877%2FFunctions%2F863300109) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [toArray](index.html#-1997751877%2FFunctions%2F863300109)(p0: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.html#-1997751877%2FFunctions%2F863300109)&gt;): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.html#-1997751877%2FFunctions%2F863300109)&gt;<br>~~open~~ ~~fun~~ ~~&lt;~~[T](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)~~&gt;~~ [~~toArray~~](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109)~~(~~~~p0~~~~:~~ [IntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109)&gt;&gt;~~)~~~~:~~ [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-online-player-collection/index.html#-1215154575%2FFunctions%2F863300109)&gt; |
| [toString](../-online-player-set/index.html#-42557405%2FFunctions%2F863300109) | [jvm]<br>open override fun [toString](../-online-player-set/index.html#-42557405%2FFunctions%2F863300109)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [unlink](index.html#37595546%2FFunctions%2F863300109) | [jvm]<br>open fun [unlink](index.html#37595546%2FFunctions%2F863300109)(p0: [LinkedList.Node](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.Node.html)&lt;Player&gt;): Player |


## Properties


| Name | Summary |
|---|---|
| [first](index.html#-106922716%2FProperties%2F863300109) | [jvm]<br>val [first](index.html#-106922716%2FProperties%2F863300109): [LinkedList.Node](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.Node.html)&lt;Player&gt; |
| [last](index.html#-485941008%2FProperties%2F863300109) | [jvm]<br>val [last](index.html#-485941008%2FProperties%2F863300109): [LinkedList.Node](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.Node.html)&lt;Player&gt; |
| [modCount](index.html#-207506750%2FProperties%2F863300109) | [jvm]<br>val [modCount](index.html#-207506750%2FProperties%2F863300109): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [plugin](plugin.html) | [jvm]<br>open override val [plugin](plugin.html): [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) |
| [size](index.html#3944933%2FProperties%2F863300109) | [jvm]<br>open override val [size](index.html#3944933%2FProperties%2F863300109): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [size](index.html#3944933%2FProperties%2F863300109) | [jvm]<br>val [size](index.html#3944933%2FProperties%2F863300109): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

