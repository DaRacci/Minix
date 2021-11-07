//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.collections](../index.md)/[ObservableMap](index.md)

# ObservableMap

[jvm]\
class [ObservableMap](index.md)&lt;[K](index.md), [V](index.md)&gt;(map: [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[K](index.md), [V](index.md)&gt;) : [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[K](index.md), [V](index.md)&gt; , [ObservableHolder](../-observable-holder/index.md)

## Functions

| Name | Summary |
|---|---|
| [clear](clear.md) | [jvm]<br>open override fun [clear](clear.md)() |
| [compute](index.md#-2125908806%2FFunctions%2F-1216412040) | [jvm]<br>open fun [compute](index.md#-2125908806%2FFunctions%2F-1216412040)(p0: [K](index.md), p1: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [K](index.md), in [V](index.md)?, out [V](index.md)?&gt;): [V](index.md)? |
| [computeIfAbsent](index.md#-2012194187%2FFunctions%2F-1216412040) | [jvm]<br>open fun [computeIfAbsent](index.md#-2012194187%2FFunctions%2F-1216412040)(p0: [K](index.md), p1: [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)&lt;in [K](index.md), out [V](index.md)&gt;): [V](index.md) |
| [computeIfPresent](index.md#1357972273%2FFunctions%2F-1216412040) | [jvm]<br>open fun [computeIfPresent](index.md#1357972273%2FFunctions%2F-1216412040)(p0: [K](index.md), p1: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [K](index.md), in [V](index.md), out [V](index.md)?&gt;): [V](index.md)? |
| [containsKey](index.md#189495335%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [containsKey](index.md#189495335%2FFunctions%2F-1216412040)(key: [K](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsValue](index.md#-337993863%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [containsValue](index.md#-337993863%2FFunctions%2F-1216412040)(value: [V](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](index.md#1890068580%2FFunctions%2F-1216412040) | [jvm]<br>open fun [forEach](index.md#1890068580%2FFunctions%2F-1216412040)(p0: [BiConsumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiConsumer.html)&lt;in [K](index.md), in [V](index.md)&gt;) |
| [get](index.md#1589144509%2FFunctions%2F-1216412040) | [jvm]<br>open operator override fun [get](index.md#1589144509%2FFunctions%2F-1216412040)(key: [K](index.md)): [V](index.md)? |
| [getOrDefault](index.md#1493482850%2FFunctions%2F-1216412040) | [jvm]<br>open fun [getOrDefault](index.md#1493482850%2FFunctions%2F-1216412040)(key: [K](index.md), defaultValue: [V](index.md)): [V](index.md) |
| [isEmpty](index.md#-1708477740%2FFunctions%2F-1216412040) | [jvm]<br>open override fun [isEmpty](index.md#-1708477740%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [merge](index.md#1519727293%2FFunctions%2F-1216412040) | [jvm]<br>open fun [merge](index.md#1519727293%2FFunctions%2F-1216412040)(p0: [K](index.md), p1: [V](index.md), p2: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [V](index.md), in [V](index.md), out [V](index.md)?&gt;): [V](index.md)? |
| [observe](../-observable-holder/observe.md) | [jvm]<br>open fun [observe](../-observable-holder/observe.md)(listener: [ObservableListener](../index.md#1056729540%2FClasslikes%2F-1216412040)) |
| [put](put.md) | [jvm]<br>open override fun [put](put.md)(key: [K](index.md), value: [V](index.md)): [V](index.md)? |
| [putAll](put-all.md) | [jvm]<br>open override fun [putAll](put-all.md)(from: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;out [K](index.md), [V](index.md)&gt;) |
| [putIfAbsent](put-if-absent.md) | [jvm]<br>open override fun [putIfAbsent](put-if-absent.md)(key: [K](index.md), value: [V](index.md)): [V](index.md)? |
| [remove](remove.md) | [jvm]<br>open override fun [remove](remove.md)(key: [K](index.md)): [V](index.md)?<br>open override fun [remove](remove.md)(key: [K](index.md), value: [V](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replace](replace.md) | [jvm]<br>open override fun [replace](replace.md)(key: [K](index.md), value: [V](index.md)): [V](index.md)?<br>open override fun [replace](replace.md)(key: [K](index.md), first: [V](index.md), second: [V](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replaceAll](index.md#-616367665%2FFunctions%2F-1216412040) | [jvm]<br>open fun [replaceAll](index.md#-616367665%2FFunctions%2F-1216412040)(p0: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [K](index.md), in [V](index.md), out [V](index.md)&gt;) |
| [runListeners](../-observable-holder/run-listeners.md) | [jvm]<br>open fun [runListeners](../-observable-holder/run-listeners.md)(action: [ObservableAction](../-observable-action/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [entries](index.md#313986111%2FProperties%2F-1216412040) | [jvm]<br>open override val [entries](index.md#313986111%2FProperties%2F-1216412040): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[MutableMap.MutableEntry](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/-mutable-entry/index.html)&lt;[K](index.md), [V](index.md)&gt;&gt; |
| [keys](index.md#-1153773961%2FProperties%2F-1216412040) | [jvm]<br>open override val [keys](index.md#-1153773961%2FProperties%2F-1216412040): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[K](index.md)&gt; |
| [listeners](listeners.md) | [jvm]<br>open override val [listeners](listeners.md): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[ObservableListener](../index.md#1056729540%2FClasslikes%2F-1216412040)&gt; |
| [size](index.md#-157521630%2FProperties%2F-1216412040) | [jvm]<br>open override val [size](index.md#-157521630%2FProperties%2F-1216412040): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [values](index.md#211311497%2FProperties%2F-1216412040) | [jvm]<br>open override val [values](index.md#211311497%2FProperties%2F-1216412040): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[V](index.md)&gt; |
