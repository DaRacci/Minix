//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.collections](../index.md)/[ExpirationMap](index.md)

# ExpirationMap

[jvm]\
interface [ExpirationMap](index.md)&lt;[K](index.md), [V](index.md)&gt; : [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[K](index.md), [V](index.md)&gt; , [WithPlugin](../../me.racci.raccicore.api.utils.extensions/-with-plugin/index.md)&lt;Plugin&gt;

## Functions

| Name | Summary |
|---|---|
| [clear](../-expiration-map-impl/index.md#1264776610%2FFunctions%2F-1216412040) | [jvm]<br>abstract fun [clear](../-expiration-map-impl/index.md#1264776610%2FFunctions%2F-1216412040)() |
| [compute](../-observable-map/index.md#-2125908806%2FFunctions%2F-1216412040) | [jvm]<br>open fun [compute](../-observable-map/index.md#-2125908806%2FFunctions%2F-1216412040)(p0: [K](index.md), p1: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [K](index.md), in [V](index.md)?, out [V](index.md)?&gt;): [V](index.md)? |
| [computeIfAbsent](../-observable-map/index.md#-2012194187%2FFunctions%2F-1216412040) | [jvm]<br>open fun [computeIfAbsent](../-observable-map/index.md#-2012194187%2FFunctions%2F-1216412040)(p0: [K](index.md), p1: [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)&lt;in [K](index.md), out [V](index.md)&gt;): [V](index.md) |
| [computeIfPresent](../-observable-map/index.md#1357972273%2FFunctions%2F-1216412040) | [jvm]<br>open fun [computeIfPresent](../-observable-map/index.md#1357972273%2FFunctions%2F-1216412040)(p0: [K](index.md), p1: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [K](index.md), in [V](index.md), out [V](index.md)?&gt;): [V](index.md)? |
| [containsKey](../-observable-map/index.md#189495335%2FFunctions%2F-1216412040) | [jvm]<br>abstract fun [containsKey](../-observable-map/index.md#189495335%2FFunctions%2F-1216412040)(key: [K](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsValue](../-observable-map/index.md#-337993863%2FFunctions%2F-1216412040) | [jvm]<br>abstract fun [containsValue](../-observable-map/index.md#-337993863%2FFunctions%2F-1216412040)(value: [V](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [expire](expire.md) | [jvm]<br>abstract fun [expire](expire.md)(key: [K](index.md), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>abstract fun [expire](expire.md)(key: [K](index.md), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), callback: [OnExpireMapCallback](../index.md#747812612%2FClasslikes%2F-1216412040)&lt;[K](index.md), [V](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Set expiration time to the key and returns true if the key is found or false if the key is not contained in the map. |
| [forEach](../-observable-map/index.md#1890068580%2FFunctions%2F-1216412040) | [jvm]<br>open fun [forEach](../-observable-map/index.md#1890068580%2FFunctions%2F-1216412040)(p0: [BiConsumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiConsumer.html)&lt;in [K](index.md), in [V](index.md)&gt;) |
| [get](../-observable-map/index.md#1589144509%2FFunctions%2F-1216412040) | [jvm]<br>abstract operator fun [get](../-observable-map/index.md#1589144509%2FFunctions%2F-1216412040)(key: [K](index.md)): [V](index.md)? |
| [getOrDefault](../-observable-map/index.md#1493482850%2FFunctions%2F-1216412040) | [jvm]<br>open fun [getOrDefault](../-observable-map/index.md#1493482850%2FFunctions%2F-1216412040)(key: [K](index.md), defaultValue: [V](index.md)): [V](index.md) |
| [isEmpty](../-observable-map/index.md#-1708477740%2FFunctions%2F-1216412040) | [jvm]<br>abstract fun [isEmpty](../-observable-map/index.md#-1708477740%2FFunctions%2F-1216412040)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [merge](../-observable-map/index.md#1519727293%2FFunctions%2F-1216412040) | [jvm]<br>open fun [merge](../-observable-map/index.md#1519727293%2FFunctions%2F-1216412040)(p0: [K](index.md), p1: [V](index.md), p2: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [V](index.md), in [V](index.md), out [V](index.md)?&gt;): [V](index.md)? |
| [missingTime](missing-time.md) | [jvm]<br>abstract fun [missingTime](missing-time.md)(key: [K](index.md)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?<br>Returns the missing time on seconds to expire the key, -1 if was not specified the expiration time before(permanent key) or null if the key is not contained in the map. |
| [put](index.md#1076499965%2FFunctions%2F-1216412040) | [jvm]<br>abstract fun [put](index.md#1076499965%2FFunctions%2F-1216412040)(key: [K](index.md), value: [V](index.md)): [V](index.md)?<br>[jvm]<br>abstract fun [put](put.md)(key: [K](index.md), value: [V](index.md), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [V](index.md)?<br>abstract fun [put](put.md)(key: [K](index.md), value: [V](index.md), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), callback: [OnExpireMapCallback](../index.md#747812612%2FClasslikes%2F-1216412040)&lt;[K](index.md), [V](index.md)&gt;): [V](index.md)?<br>Associates the specified [value](put.md) with the specified [key](put.md) in the map with an expiration time. |
| [putAll](../-expiration-map-impl/index.md#-1770992861%2FFunctions%2F-1216412040) | [jvm]<br>abstract fun [putAll](../-expiration-map-impl/index.md#-1770992861%2FFunctions%2F-1216412040)(from: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;out [K](index.md), [V](index.md)&gt;) |
| [putIfAbsent](../-expiration-map-impl/index.md#-255529517%2FFunctions%2F-1216412040) | [jvm]<br>open fun [putIfAbsent](../-expiration-map-impl/index.md#-255529517%2FFunctions%2F-1216412040)(p0: [K](index.md), p1: [V](index.md)): [V](index.md)? |
| [remove](index.md#-121413961%2FFunctions%2F-1216412040) | [jvm]<br>abstract fun [remove](index.md#-121413961%2FFunctions%2F-1216412040)(key: [K](index.md)): [V](index.md)?<br>open fun [remove](../-expiration-map-impl/index.md#351754838%2FFunctions%2F-1216412040)(key: [K](index.md), value: [V](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replace](../-expiration-map-impl/index.md#1894614946%2FFunctions%2F-1216412040) | [jvm]<br>open fun [replace](../-expiration-map-impl/index.md#1894614946%2FFunctions%2F-1216412040)(p0: [K](index.md), p1: [V](index.md)): [V](index.md)?<br>open fun [replace](../-expiration-map-impl/index.md#-1618274495%2FFunctions%2F-1216412040)(p0: [K](index.md), p1: [V](index.md), p2: [V](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replaceAll](../-observable-map/index.md#-616367665%2FFunctions%2F-1216412040) | [jvm]<br>open fun [replaceAll](../-observable-map/index.md#-616367665%2FFunctions%2F-1216412040)(p0: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [K](index.md), in [V](index.md), out [V](index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [entries](../-observable-map/index.md#313986111%2FProperties%2F-1216412040) | [jvm]<br>abstract override val [entries](../-observable-map/index.md#313986111%2FProperties%2F-1216412040): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[MutableMap.MutableEntry](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/-mutable-entry/index.html)&lt;[K](index.md), [V](index.md)&gt;&gt; |
| [keys](../-observable-map/index.md#-1153773961%2FProperties%2F-1216412040) | [jvm]<br>abstract override val [keys](../-observable-map/index.md#-1153773961%2FProperties%2F-1216412040): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[K](index.md)&gt; |
| [plugin](../../me.racci.raccicore.api.utils.extensions/-with-plugin/plugin.md) | [jvm]<br>abstract val [plugin](../../me.racci.raccicore.api.utils.extensions/-with-plugin/plugin.md): Plugin |
| [size](../-observable-map/index.md#-157521630%2FProperties%2F-1216412040) | [jvm]<br>abstract val [size](../-observable-map/index.md#-157521630%2FProperties%2F-1216412040): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [values](../-observable-map/index.md#211311497%2FProperties%2F-1216412040) | [jvm]<br>abstract override val [values](../-observable-map/index.md#211311497%2FProperties%2F-1216412040): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[V](index.md)&gt; |

## Inheritors

| Name |
|---|
| [ExpirationMapImpl](../-expiration-map-impl/index.md) |
