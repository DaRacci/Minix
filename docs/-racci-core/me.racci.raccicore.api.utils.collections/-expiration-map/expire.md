---
title: expire
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[ExpirationMap](index.html)/[expire](expire.html)



# expire



[jvm]\
abstract fun [expire](expire.html)(key: [K](index.html), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)



Set expiration time to the key and returns true if the key is found or false if the key is not contained in the map.





[jvm]\
abstract fun [expire](expire.html)(key: [K](index.html), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), callback: [OnExpireMapCallback](../index.html#-1536602664%2FClasslikes%2F863300109)&lt;[K](index.html), [V](index.html)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)



Set expiration time to the key and returns true if the key is found or false if the key is not contained in the map.



[time](expire.html) in seconds. [callback](expire.html) is called when the key expires.




