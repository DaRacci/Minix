---
title: ObservableMap
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[ObservableMap](index.html)



# ObservableMap



[jvm]\
class [ObservableMap](index.html)&lt;[K](index.html), [V](index.html)&gt;(map: [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[K](index.html), [V](index.html)&gt;) : [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[K](index.html), [V](index.html)&gt; , [ObservableHolder](../-observable-holder/index.html)



## Functions


| Name | Summary |
|---|---|
| [clear](clear.html) | [jvm]<br>open override fun [clear](clear.html)() |
| [compute](index.html#-2125908806%2FFunctions%2F863300109) | [jvm]<br>open fun [compute](index.html#-2125908806%2FFunctions%2F863300109)(p0: [K](index.html), p1: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [K](index.html), in [V](index.html)?, out [V](index.html)?&gt;): [V](index.html)? |
| [computeIfAbsent](index.html#-2012194187%2FFunctions%2F863300109) | [jvm]<br>open fun [computeIfAbsent](index.html#-2012194187%2FFunctions%2F863300109)(p0: [K](index.html), p1: [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html)&lt;in [K](index.html), out [V](index.html)&gt;): [V](index.html) |
| [computeIfPresent](index.html#1357972273%2FFunctions%2F863300109) | [jvm]<br>open fun [computeIfPresent](index.html#1357972273%2FFunctions%2F863300109)(p0: [K](index.html), p1: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [K](index.html), in [V](index.html), out [V](index.html)?&gt;): [V](index.html)? |
| [containsKey](index.html#189495335%2FFunctions%2F863300109) | [jvm]<br>open override fun [containsKey](index.html#189495335%2FFunctions%2F863300109)(key: [K](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsValue](index.html#-337993863%2FFunctions%2F863300109) | [jvm]<br>open override fun [containsValue](index.html#-337993863%2FFunctions%2F863300109)(value: [V](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](index.html#1890068580%2FFunctions%2F863300109) | [jvm]<br>open fun [forEach](index.html#1890068580%2FFunctions%2F863300109)(p0: [BiConsumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiConsumer.html)&lt;in [K](index.html), in [V](index.html)&gt;) |
| [get](index.html#1589144509%2FFunctions%2F863300109) | [jvm]<br>open operator override fun [get](index.html#1589144509%2FFunctions%2F863300109)(key: [K](index.html)): [V](index.html)? |
| [getOrDefault](index.html#1493482850%2FFunctions%2F863300109) | [jvm]<br>open fun [getOrDefault](index.html#1493482850%2FFunctions%2F863300109)(key: [K](index.html), defaultValue: [V](index.html)): [V](index.html) |
| [isEmpty](index.html#-1708477740%2FFunctions%2F863300109) | [jvm]<br>open override fun [isEmpty](index.html#-1708477740%2FFunctions%2F863300109)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [merge](index.html#1519727293%2FFunctions%2F863300109) | [jvm]<br>open fun [merge](index.html#1519727293%2FFunctions%2F863300109)(p0: [K](index.html), p1: [V](index.html), p2: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [V](index.html), in [V](index.html), out [V](index.html)?&gt;): [V](index.html)? |
| [observe](../-observable-holder/observe.html) | [jvm]<br>open fun [observe](../-observable-holder/observe.html)(listener: [ObservableListener](../index.html#290302064%2FClasslikes%2F863300109)) |
| [put](put.html) | [jvm]<br>open override fun [put](put.html)(key: [K](index.html), value: [V](index.html)): [V](index.html)? |
| [putAll](put-all.html) | [jvm]<br>open override fun [putAll](put-all.html)(from: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;out [K](index.html), [V](index.html)&gt;) |
| [putIfAbsent](put-if-absent.html) | [jvm]<br>open override fun [putIfAbsent](put-if-absent.html)(key: [K](index.html), value: [V](index.html)): [V](index.html)? |
| [remove](remove.html) | [jvm]<br>open override fun [remove](remove.html)(key: [K](index.html)): [V](index.html)?<br>open override fun [remove](remove.html)(key: [K](index.html), value: [V](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replace](replace.html) | [jvm]<br>open override fun [replace](replace.html)(key: [K](index.html), value: [V](index.html)): [V](index.html)?<br>open override fun [replace](replace.html)(key: [K](index.html), first: [V](index.html), second: [V](index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [replaceAll](index.html#-616367665%2FFunctions%2F863300109) | [jvm]<br>open fun [replaceAll](index.html#-616367665%2FFunctions%2F863300109)(p0: [BiFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/BiFunction.html)&lt;in [K](index.html), in [V](index.html), out [V](index.html)&gt;) |
| [runListeners](../-observable-holder/run-listeners.html) | [jvm]<br>open fun [runListeners](../-observable-holder/run-listeners.html)(action: [ObservableAction](../-observable-action/index.html)) |


## Properties


| Name | Summary |
|---|---|
| [entries](index.html#313986111%2FProperties%2F863300109) | [jvm]<br>open override val [entries](index.html#313986111%2FProperties%2F863300109): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[MutableMap.MutableEntry](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/-mutable-entry/index.html)&lt;[K](index.html), [V](index.html)&gt;&gt; |
| [keys](index.html#-1153773961%2FProperties%2F863300109) | [jvm]<br>open override val [keys](index.html#-1153773961%2FProperties%2F863300109): [MutableSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)&lt;[K](index.html)&gt; |
| [listeners](listeners.html) | [jvm]<br>open override val [listeners](listeners.html): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[ObservableListener](../index.html#290302064%2FClasslikes%2F863300109)&gt; |
| [size](index.html#-157521630%2FProperties%2F863300109) | [jvm]<br>open override val [size](index.html#-157521630%2FProperties%2F863300109): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [values](index.html#211311497%2FProperties%2F863300109) | [jvm]<br>open override val [values](index.html#211311497%2FProperties%2F863300109): [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[V](index.html)&gt; |

