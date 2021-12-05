---
title: me.racci.raccicore.api.lifecycle
---
//[RacciCore](../../index.html)/[me.racci.raccicore.api.lifecycle](index.html)



# Package me.racci.raccicore.api.lifecycle



## Types


| Name | Summary |
|---|---|
| [Lifecycle](-lifecycle/index.html) | [jvm]<br>data class [Lifecycle](-lifecycle/index.html)(priority: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), listener: [PluginLifecycle](index.html#1671498386%2FClasslikes%2F863300109)) : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparable/index.html)&lt;[Lifecycle](-lifecycle/index.html)&gt; <br>Holds a lifecycle listener class and its priority |
| [LifecycleEvent](-lifecycle-event/index.html) | [jvm]<br>enum [LifecycleEvent](-lifecycle-event/index.html) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[LifecycleEvent](-lifecycle-event/index.html)&gt; <br>Lifecycle enums. |
| [LifecycleListener](-lifecycle-listener/index.html) | [jvm]<br>interface [LifecycleListener](-lifecycle-listener/index.html)&lt;[P](-lifecycle-listener/index.html) : [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.html)&gt; : [SuspendFunction1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-suspend-function1/index.html)&lt;[LifecycleEvent](-lifecycle-event/index.html), [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt; , [WithPlugin](../me.racci.raccicore.api.extensions/-with-plugin/index.html)&lt;[P](-lifecycle-listener/index.html)&gt; |
| [LifecycleUtils](-lifecycle-utils/index.html) | [jvm]<br>object [LifecycleUtils](-lifecycle-utils/index.html) |
| [PluginLifecycle](index.html#1671498386%2FClasslikes%2F863300109) | [jvm]<br>typealias [PluginLifecycle](index.html#1671498386%2FClasslikes%2F863300109) = suspend ([LifecycleEvent](-lifecycle-event/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

