---
title: registerLifecycles
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.core](../index.html)/[RacciCore](index.html)/[registerLifecycles](register-lifecycles.html)



# registerLifecycles



[jvm]\
open suspend override fun [registerLifecycles](register-lifecycles.html)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[LifecycleListener](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.html)&lt;*&gt;, [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;&gt;



The returned life of [LifecycleListener](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.html)'s will be registered, and enabled during the enable process.




