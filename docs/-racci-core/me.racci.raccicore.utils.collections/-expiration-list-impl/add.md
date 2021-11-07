//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.collections](../index.md)/[ExpirationListImpl](index.md)/[add](add.md)

# add

[jvm]\
open override fun [add](add.md)(element: [E](index.md), expireTime: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), onExpire: [OnExpireCallback](../index.md#1412320920%2FClasslikes%2F-1216412040)&lt;[E](index.md)&gt;?)

Add the element to the list with an expiration time.

[expireTime](add.md) in seconds callback [onExpire](add.md) is called when the element expires.
