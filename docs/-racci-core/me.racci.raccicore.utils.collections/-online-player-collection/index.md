//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.collections](../index.md)/[OnlinePlayerCollection](index.md)

# OnlinePlayerCollection

[jvm]\
interface [OnlinePlayerCollection](index.md) : [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;Player&gt; , [KListener](../../me.racci.raccicore.utils.extensions/-k-listener/index.md)&lt;Plugin&gt;

## Functions

| Name | Summary |
|---|---|
| [add](index.md#-1363033720%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [add](index.md#-1363033720%2FFunctions%2F-519281799)(element: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>[jvm]<br>open fun [add](add.md)(player: Player, whenPlayerQuit: [WhenPlayerQuitCollectionCallback](../index.md#770480590%2FClasslikes%2F-519281799)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Adds a new Player to the collection with a callback for when the player quits the server. |
| [addAll](index.md#1674017175%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [addAll](index.md#1674017175%2FFunctions%2F-519281799)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [checkRegistration](check-registration.md) | [jvm]<br>open fun [checkRegistration](check-registration.md)() |
| [clear](index.md#1405312578%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [clear](index.md#1405312578%2FFunctions%2F-519281799)() |
| [clearQuiting](clear-quiting.md) | [jvm]<br>open fun [clearQuiting](clear-quiting.md)()<br>Clear the collection calling all [WhenPlayerQuitCollectionCallback](../index.md#770480590%2FClasslikes%2F-519281799) from the Players. |
| [contains](index.md#-1747698034%2FFunctions%2F-519281799) | [jvm]<br>abstract operator fun [contains](index.md#-1747698034%2FFunctions%2F-519281799)(element: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.md#-955304675%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [containsAll](index.md#-955304675%2FFunctions%2F-519281799)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](index.md#-1461011823%2FFunctions%2F-519281799) | [jvm]<br>open fun [forEach](index.md#-1461011823%2FFunctions%2F-519281799)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in Player&gt;) |
| [isEmpty](index.md#-719293276%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [isEmpty](index.md#-719293276%2FFunctions%2F-519281799)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](index.md#1177836957%2FFunctions%2F-519281799) | [jvm]<br>abstract operator override fun [iterator](index.md#1177836957%2FFunctions%2F-519281799)(): [MutableIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-iterator/index.html)&lt;Player&gt; |
| [parallelStream](index.md#-1592339412%2FFunctions%2F-519281799) | [jvm]<br>open fun [parallelStream](index.md#-1592339412%2FFunctions%2F-519281799)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;Player&gt; |
| [quit](quit.md) | [jvm]<br>open fun [quit](quit.md)(player: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Removes the player from the collection, calling the [WhenPlayerQuitCollectionCallback](../index.md#770480590%2FClasslikes%2F-519281799) provided. |
| [remove](index.md#-1832428191%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [remove](index.md#-1832428191%2FFunctions%2F-519281799)(element: Player): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeAll](index.md#1885396784%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [removeAll](index.md#1885396784%2FFunctions%2F-519281799)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [removeIf](index.md#-1875219347%2FFunctions%2F-519281799) | [jvm]<br>open fun [removeIf](index.md#-1875219347%2FFunctions%2F-519281799)(p0: [Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html)&lt;in Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [retainAll](index.md#-667323121%2FFunctions%2F-519281799) | [jvm]<br>abstract fun [retainAll](index.md#-667323121%2FFunctions%2F-519281799)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;Player&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [spliterator](index.md#1956926474%2FFunctions%2F-519281799) | [jvm]<br>open override fun [spliterator](index.md#1956926474%2FFunctions%2F-519281799)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;Player&gt; |
| [stream](index.md#135225651%2FFunctions%2F-519281799) | [jvm]<br>open fun [stream](index.md#135225651%2FFunctions%2F-519281799)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;Player&gt; |
| [toArray](index.md#-1215154575%2FFunctions%2F-519281799) | [jvm]<br>~~open~~ ~~fun~~ ~~&lt;~~[T](index.md#-1215154575%2FFunctions%2F-519281799) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)~~&gt;~~ [~~toArray~~](index.md#-1215154575%2FFunctions%2F-519281799)~~(~~~~p0~~~~:~~ [IntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.md#-1215154575%2FFunctions%2F-519281799)&gt;&gt;~~)~~~~:~~ [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.md#-1215154575%2FFunctions%2F-519281799)&gt; |

## Properties

| Name | Summary |
|---|---|
| [plugin](../../me.racci.raccicore.utils.extensions/-with-plugin/plugin.md) | [jvm]<br>abstract val [plugin](../../me.racci.raccicore.utils.extensions/-with-plugin/plugin.md): Plugin |
| [size](index.md#-113084078%2FProperties%2F-519281799) | [jvm]<br>abstract val [size](index.md#-113084078%2FProperties%2F-519281799): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Inheritors

| Name |
|---|
| [OnlinePlayerList](../-online-player-list/index.md) |
| [OnlinePlayerSet](../-online-player-set/index.md) |
