---
title: missingTime
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[ExpirationMap](index.html)/[missingTime](missing-time.html)



# missingTime



[jvm]\
abstract fun [missingTime](missing-time.html)(key: [K](index.html)): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)?



Returns the missing time on seconds to expire the key, -1 if was not specified the expiration time before(permanent key) or null if the key is not contained in the map.




