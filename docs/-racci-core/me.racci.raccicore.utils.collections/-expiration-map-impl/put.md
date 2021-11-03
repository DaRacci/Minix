//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.collections](../index.md)/[ExpirationMapImpl](index.md)/[put](put.md)

# put

[jvm]\
open override fun [put](put.md)(key: [K](index.md), value: [V](index.md)): [V](index.md)?

[jvm]\
open override fun [put](put.md)(key: [K](index.md), value: [V](index.md), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [V](index.md)?

Associates the specified [value](put.md) with the specified [key](put.md) in the map with an expiration time.

#### Return

the previous value associated with the key, or null if the key was not present in the map.

[jvm]\
open override fun [put](put.md)(key: [K](index.md), value: [V](index.md), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), callback: [OnExpireMapCallback](../index.md#747812612%2FClasslikes%2F-519281799)&lt;[K](index.md), [V](index.md)&gt;): [V](index.md)?

Associates the specified [value](put.md) with the specified [key](put.md) in the map with an expiration time.

[time](put.md) in seconds. [callback](put.md) is called when the key expires.

#### Return

the previous value associated with the key, or null if the key was not present in the map.
