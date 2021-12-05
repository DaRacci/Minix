---
title: clear
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[CollectionUtils](index.html)/[clear](clear.html)



# clear



[jvm]\
inline fun &lt;[T](clear.html)&gt; [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[T](clear.html)&gt;.[clear](clear.html)(onRemove: ([T](clear.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))



Compute an action with each item of this collection before removing it from the collection.



## Parameters


jvm

| | |
|---|---|
| T | The Object Type. |
| onRemove | The Action to execute. |





[jvm]\
inline fun &lt;[K](clear.html), [V](clear.html)&gt; [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[K](clear.html), [V](clear.html)&gt;.[clear](clear.html)(onRemove: ([K](clear.html), [V](clear.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))



Compute an action with each entry of this map before removing it from the collection.



## Parameters


jvm

| | |
|---|---|
| K | The key type. |
| V | The value type. |
| onRemove | The Action to execute. |




