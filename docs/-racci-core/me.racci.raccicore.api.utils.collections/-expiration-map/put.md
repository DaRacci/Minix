//[RacciCore](../../../index.md)/[me.racci.raccicore.api.utils.collections](../index.md)/[ExpirationMap](index.md)/[put](put.md)

# put

[jvm]\
abstract fun [put](put.md)(key: [K](index.md), value: [V](index.md), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [V](index.md)?

Associates the specified [value](put.md) with the specified [key](put.md) in the map with an expiration time.

#### Return

the previous value associated with the key, or null if the key was not present in the map.

[jvm]\
abstract fun [put](put.md)(key: [K](index.md), value: [V](index.md), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), callback: [OnExpireMapCallback](../index.md#-1536602664%2FClasslikes%2F-1216412040)&lt;[K](index.md), [V](index.md)
&gt;): [V](index.md)?

Associates the specified [value](put.md) with the specified [key](put.md) in the map with an expiration time.

[time](put.md) in seconds. [callback](put.md) is called when the key expires.

#### Return

the previous value associated with the key, or null if the key was not present in the map.
