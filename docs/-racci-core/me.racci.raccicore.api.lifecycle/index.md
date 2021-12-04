//[RacciCore](../../index.md)/[me.racci.raccicore.api.lifecycle](index.md)

# Package me.racci.raccicore.api.lifecycle

## Types

| Name | Summary |
|---|---|
| [Lifecycle](-lifecycle/index.md) | [jvm]<br>data class [Lifecycle](-lifecycle/index.md)(priority: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), listener: [PluginLifecycle](index.md#1671498386%2FClasslikes%2F-1216412040)) : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparable/index.html)&lt;[Lifecycle](-lifecycle/index.md)&gt; <br>Holds a lifecycle listener class and its priority |
| [LifecycleEvent](-lifecycle-event/index.md) | [jvm]<br>enum [LifecycleEvent](-lifecycle-event/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[LifecycleEvent](-lifecycle-event/index.md)&gt; <br>Lifecycle enums. |
| [LifecycleListener](-lifecycle-listener/index.md) | [jvm]<br>interface [LifecycleListener](-lifecycle-listener/index.md)&lt;[P](-lifecycle-listener/index.md) : [RacciPlugin](../me.racci.raccicore.api.plugin/-racci-plugin/index.md)&gt; : [SuspendFunction1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-suspend-function1/index.html)&lt;[LifecycleEvent](-lifecycle-event/index.md), [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt; , [WithPlugin](../me.racci.raccicore.api.extensions/-with-plugin/index.md)&lt;[P](-lifecycle-listener/index.md)&gt; |
| [LifecycleUtils](-lifecycle-utils/index.md) | [jvm]<br>object [LifecycleUtils](-lifecycle-utils/index.md) |
| [PluginLifecycle](index.md#1671498386%2FClasslikes%2F-1216412040) | [jvm]<br>typealias [PluginLifecycle](index.md#1671498386%2FClasslikes%2F-1216412040) = suspend ([LifecycleEvent](-lifecycle-event/index.md)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
