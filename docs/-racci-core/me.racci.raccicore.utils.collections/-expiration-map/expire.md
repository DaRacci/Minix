//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.collections](../index.md)/[ExpirationMap](index.md)/[expire](expire.md)

# expire

[jvm]\
abstract fun [expire](expire.md)(key: [K](index.md), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Set expiration time to the key and returns true if the key is found or false if the key is not contained in the map.

[jvm]\
abstract fun [expire](expire.md)(key: [K](index.md), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), callback: [OnExpireMapCallback](../index.md#747812612%2FClasslikes%2F-1216412040)&lt;[K](index.md), [V](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Set expiration time to the key and returns true if the key is found or false if the key is not contained in the map.

[time](expire.md) in seconds. [callback](expire.md) is called when the key expires.
