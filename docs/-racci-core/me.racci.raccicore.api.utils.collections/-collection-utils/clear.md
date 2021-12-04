//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.collections](../index.md)/[CollectionUtils](index.md)/[clear](clear.md)

# clear

[jvm]\
inline fun &lt;[T](clear.md)&gt; [MutableCollection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-collection/index.html)&lt;[T](clear.md)&gt;.[clear](clear.md)(onRemove: ([T](clear.md))
-&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

Compute an action with each item of this collection before removing it from the collection.

## Parameters

jvm

| | |
|---|---|
| T | The Object Type. |
| onRemove | The Action to execute. |

[jvm]\
inline fun &lt;[K](clear.md), [V](clear.md)&gt; [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[K](clear.md), [V](clear.md)&gt;.[clear](clear.md)(onRemove: ([K](clear.md), [V](clear.md))
-&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

Compute an action with each entry of this map before removing it from the collection.

## Parameters

jvm

| | |
|---|---|
| K | The key type. |
| V | The value type. |
| onRemove | The Action to execute. |
