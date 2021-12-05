---
title: put
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.utils.collections](../index.html)/[ExpirationMapImpl](index.html)/[put](put.html)



# put



[jvm]\
open override fun [put](put.html)(key: [K](index.html), value: [V](index.html)): [V](index.html)?





[jvm]\
open override fun [put](put.html)(key: [K](index.html), value: [V](index.html), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [V](index.html)?



Associates the specified [value](put.html) with the specified [key](put.html) in the map with an expiration time.



#### Return



the previous value associated with the key, or null if the key was not present in the map.





[jvm]\
open override fun [put](put.html)(key: [K](index.html), value: [V](index.html), time: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), callback: [OnExpireMapCallback](../index.html#-1536602664%2FClasslikes%2F863300109)&lt;[K](index.html), [V](index.html)&gt;): [V](index.html)?



Associates the specified [value](put.html) with the specified [key](put.html) in the map with an expiration time.



[time](put.html) in seconds. [callback](put.html) is called when the key expires.



#### Return



the previous value associated with the key, or null if the key was not present in the map.




