//[RacciCore](../../../index.md)/[me.racci.raccicore.utils.collections](../index.md)/[ExpirationList](index.md)/[addFirst](add-first.md)

# addFirst

[jvm]\
abstract fun [addFirst](add-first.md)(element: [E](index.md), expireTime: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), onExpire: [OnExpireCallback](../index.md#1412320920%2FClasslikes%2F-519281799)&lt;[E](index.md)&gt;? = null)

Add the element in the start of list with an expiration time.

[expireTime](add-first.md) in seconds callback [onExpire](add-first.md) is called when the element expires.
