---
title: CollectionUtils
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[CollectionUtils](index.html)



# CollectionUtils



[jvm]\
object [CollectionUtils](index.html)

Utilities for Generic Collections



## Functions


| Name | Summary |
|---|---|
| [clear](clear.html) | [jvm]<br>inline fun &lt;[T](clear.html)&gt; [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[T](clear.html)&gt;.[clear](clear.html)(onRemove: ([T](clear.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Compute an action with each item of this collection before removing it from the collection.<br>[jvm]<br>inline fun &lt;[K](clear.html), [V](clear.html)&gt; [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[K](clear.html), [V](clear.html)&gt;.[clear](clear.html)(onRemove: ([K](clear.html), [V](clear.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>Compute an action with each entry of this map before removing it from the collection. |
| [containsIgnoreCase](contains-ignore-case.html) | [jvm]<br>fun [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;.[containsIgnoreCase](contains-ignore-case.html)(element: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the array contains the [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) by IgnoreCase.<br>[jvm]<br>fun [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;.[containsIgnoreCase](contains-ignore-case.html)(element: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the collection contains the [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) by IgnoreCase. |
| [containsKeyIgnoreCase](contains-key-ignore-case.html) | [jvm]<br>fun &lt;[V](contains-key-ignore-case.html)&gt; [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [V](contains-key-ignore-case.html)&gt;.[containsKeyIgnoreCase](contains-key-ignore-case.html)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the map contains the [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) as a key by IgnoreCase. |
| [getAs](get-as.html) | [jvm]<br>fun &lt;[T](get-as.html)&gt; [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;*, *&gt;.[getAs](get-as.html)(key: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [T](get-as.html) |
| [getAsOr](get-as-or.html) | [jvm]<br>fun &lt;[T](get-as-or.html)&gt; [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;*, *&gt;.[getAsOr](get-as-or.html)(key: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), def: [T](get-as-or.html)): [T](get-as-or.html) |
| [getAsOrNull](get-as-or-null.html) | [jvm]<br>fun &lt;[T](get-as-or-null.html)&gt; [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;*, *&gt;.[getAsOrNull](get-as-or-null.html)(key: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [T](get-as-or-null.html)? |
| [getIgnoreCase](get-ignore-case.html) | [jvm]<br>fun &lt;[V](get-ignore-case.html)&gt; [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [V](get-ignore-case.html)&gt;.[getIgnoreCase](get-ignore-case.html)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [V](get-ignore-case.html)?<br>Attempts to find and retrieve the key matching the [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) by IgnoreCase. |

