//[RacciCore](../../../index.md)/[me.racci.raccicore.api.plugin](../index.md)/[RacciPlugin](index.md)/[registerLifecycles](register-lifecycles.md)

# registerLifecycles

[jvm]\
open suspend fun [registerLifecycles](register-lifecycles.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[LifecycleListener](
../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.md)&lt;*&gt;, [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;&gt;

The returned life of [LifecycleListener](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.md)'s will be registered, and enabled during the enable process.
