---
title: ExpirationMap
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[ExpirationMap](index.html)



# ExpirationMap



[jvm]\
interface [ExpirationMap](index.html)&lt;[K](index.html), [V](index.html)&gt; : [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[K](index.html), [V](index.html)&gt; , [WithPlugin](../../me.racci.raccicore.api.extensions/-with-plugin/index.html)&lt;[RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)&gt;



## Functions


| Name | Summary |
|---|---|
| [clear](../-expiration-map-impl/index.html#1264776610%2FFunctions%2F863300109) | [jvm]<br>abstract fun [clear](../-expiration-map-impl/index.html#1264776610%2FFunctions%2F863300109)() |
| [compute](../-observable-map/index.html#-2125908806%2FFunctions%2F863300109) | [jvm]<br>open fun [compute](../-observable-map/index.html#-2125908806%2FFunctions%2F863300109)(p0: [K](index.html), p1: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [K](index.html), in [V](index.html)?, out [V](index.html)?&gt;): [V](index.html)? |
| [computeIfAbsent](../-observable-map/index.html#-2012194187%2FFunctions%2F863300109) | [jvm]<br>open fun [computeIfAbsent](../-observable-map/index.html#-2012194187%2FFunctions%2F863300109)(p0: [K](index.html), p1: [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)&lt;in [K](index.html), out [V](index.html)&gt;): [V](index.html) |
| [computeIfPresent](../-observable-map/index.html#1357972273%2FFunctions%2F863300109) | [jvm]<br>open fun [computeIfPresent](../-observable-map/index.html#1357972273%2FFunctions%2F863300109)(p0: [K](index.html), p1: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [K](index.html), in [V](index.html), out [V](index.html)?&gt;): [V](index.html)? |
| [containsKey](../-observable-map/index.html#189495335%2FFunctions%2F863300109) | [jvm]<br>abstract fun [containsKey](../-observable-map/index.html#189495335%2FFunctions%2F863300109)(key: [K](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsValue](../-observable-map/index.html#-337993863%2FFunctions%2F863300109) | [jvm]<br>abstract fun [containsValue](../-observable-map/index.html#-337993863%2FFunctions%2F863300109)(value: [V](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [expire](expire.html) | [jvm]<br>abstract fun [expire](expire.html)(key: [K](index.html), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>abstract fun [expire](expire.html)(key: [K](index.html), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), callback: [OnExpireMapCallback](../index.html#-1536602664%2FClasslikes%2F863300109)&lt;[K](index.html), [V](index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Set expiration time to the key and returns true if the key is found or false if the key is not contained in the map. |
| [forEach](../-observable-map/index.html#1890068580%2FFunctions%2F863300109) | [jvm]<br>open fun [forEach](../-observable-map/index.html#1890068580%2FFunctions%2F863300109)(p0: [BiConsumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiConsumer.html)&lt;in [K](index.html), in [V](index.html)&gt;) |
| [get](../-observable-map/index.html#1589144509%2FFunctions%2F863300109) | [jvm]<br>abstract operator fun [get](../-observable-map/index.html#1589144509%2FFunctions%2F863300109)(key: [K](index.html)): [V](index.html)? |
| [getOrDefault](../-observable-map/index.html#1493482850%2FFunctions%2F863300109) | [jvm]<br>open fun [getOrDefault](../-observable-map/index.html#1493482850%2FFunctions%2F863300109)(key: [K](index.html), defaultValue: [V](index.html)): [V](index.html) |
| [isEmpty](../-observable-map/index.html#-1708477740%2FFunctions%2F863300109) | [jvm]<br>abstract fun [isEmpty](../-observable-map/index.html#-1708477740%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [merge](../-observable-map/index.html#1519727293%2FFunctions%2F863300109) | [jvm]<br>open fun [merge](../-observable-map/index.html#1519727293%2FFunctions%2F863300109)(p0: [K](index.html), p1: [V](index.html), p2: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [V](index.html), in [V](index.html), out [V](index.html)?&gt;): [V](index.html)? |
| [missingTime](missing-time.html) | [jvm]<br>abstract fun [missingTime](missing-time.html)(key: [K](index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?<br>Returns the missing time on seconds to expire the key, -1 if was not specified the expiration time before(permanent key) or null if the key is not contained in the map. |
| [put](index.html#1076499965%2FFunctions%2F863300109) | [jvm]<br>abstract fun [put](index.html#1076499965%2FFunctions%2F863300109)(key: [K](index.html), value: [V](index.html)): [V](index.html)?<br>[jvm]<br>abstract fun [put](put.html)(key: [K](index.html), value: [V](index.html), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [V](index.html)?<br>abstract fun [put](put.html)(key: [K](index.html), value: [V](index.html), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), callback: [OnExpireMapCallback](../index.html#-1536602664%2FClasslikes%2F863300109)&lt;[K](index.html), [V](index.html)&gt;): [V](index.html)?<br>Associates the specified [value](put.html) with the specified [key](put.html) in the map with an expiration time. |
| [putAll](../-expiration-map-impl/index.html#-1770992861%2FFunctions%2F863300109) | [jvm]<br>abstract fun [putAll](../-expiration-map-impl/index.html#-1770992861%2FFunctions%2F863300109)(from: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;out [K](index.html), [V](index.html)&gt;) |
| [putIfAbsent](../-expiration-map-impl/index.html#-255529517%2FFunctions%2F863300109) | [jvm]<br>open fun [putIfAbsent](../-expiration-map-impl/index.html#-255529517%2FFunctions%2F863300109)(p0: [K](index.html), p1: [V](index.html)): [V](index.html)? |
| [remove](index.html#-121413961%2FFunctions%2F863300109) | [jvm]<br>abstract fun [remove](index.html#-121413961%2FFunctions%2F863300109)(key: [K](index.html)): [V](index.html)?<br>open fun [remove](../-expiration-map-impl/index.html#351754838%2FFunctions%2F863300109)(key: [K](index.html), value: [V](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replace](../-expiration-map-impl/index.html#1894614946%2FFunctions%2F863300109) | [jvm]<br>open fun [replace](../-expiration-map-impl/index.html#1894614946%2FFunctions%2F863300109)(p0: [K](index.html), p1: [V](index.html)): [V](index.html)?<br>open fun [replace](../-expiration-map-impl/index.html#-1618274495%2FFunctions%2F863300109)(p0: [K](index.html), p1: [V](index.html), p2: [V](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replaceAll](../-observable-map/index.html#-616367665%2FFunctions%2F863300109) | [jvm]<br>open fun [replaceAll](../-observable-map/index.html#-616367665%2FFunctions%2F863300109)(p0: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [K](index.html), in [V](index.html), out [V](index.html)&gt;) |


## Properties


| Name | Summary |
|---|---|
| [entries](../-observable-map/index.html#313986111%2FProperties%2F863300109) | [jvm]<br>abstract override val [entries](../-observable-map/index.html#313986111%2FProperties%2F863300109): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[MutableMap.MutableEntry](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/-mutable-entry/index.html)&lt;[K](index.html), [V](index.html)&gt;&gt; |
| [keys](../-observable-map/index.html#-1153773961%2FProperties%2F863300109) | [jvm]<br>abstract override val [keys](../-observable-map/index.html#-1153773961%2FProperties%2F863300109): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[K](index.html)&gt; |
| [plugin](../../me.racci.raccicore.api.extensions/-with-plugin/plugin.html) | [jvm]<br>abstract val [plugin](../../me.racci.raccicore.api.extensions/-with-plugin/plugin.html): [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) |
| [size](../-observable-map/index.html#-157521630%2FProperties%2F863300109) | [jvm]<br>abstract val [size](../-observable-map/index.html#-157521630%2FProperties%2F863300109): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [values](../-observable-map/index.html#211311497%2FProperties%2F863300109) | [jvm]<br>abstract override val [values](../-observable-map/index.html#211311497%2FProperties%2F863300109): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[V](index.html)&gt; |


## Inheritors


| Name |
|---|
| [ExpirationMapImpl](../-expiration-map-impl/index.html) |

