//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.collections](../index.md)/[ExpirationMap](index.md)/[missingTime](missing-time.md)

# missingTime

[jvm]\
abstract fun [missingTime](missing-time.md)(key: [K](index.md)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?

Returns the missing time on seconds to expire the key, -1 if was not specified the expiration time before(permanent key) or null if the key is not contained in the map.
