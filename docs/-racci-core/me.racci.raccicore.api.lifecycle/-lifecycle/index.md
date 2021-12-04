//[RacciCore](../../../index.md)/[me.racci.raccicore.api.lifecycle](../index.md)/[Lifecycle](index.md)

# Lifecycle

[jvm]\
data class [Lifecycle](index.md)(priority: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html),
listener: [PluginLifecycle](../index.md#1671498386%2FClasslikes%2F-1216412040)) : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparable/index.html)&lt;[Lifecycle](index.md)&gt;

Holds a lifecycle listener class and its priority

## Constructors

| | |
|---|---|
| [Lifecycle](-lifecycle.md) | [jvm]<br>fun [Lifecycle](-lifecycle.md)(priority: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), listener: [PluginLifecycle](../index.md#1671498386%2FClasslikes%2F-1216412040)) |

## Functions

| Name | Summary |
|---|---|
| [compareTo](compare-to.md) | [jvm]<br>open operator override fun [compareTo](compare-to.md)(other: [Lifecycle](index.md)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Properties

| Name | Summary |
|---|---|
| [listener](listener.md) | [jvm]<br>val [listener](listener.md): [PluginLifecycle](../index.md#1671498386%2FClasslikes%2F-1216412040) |
| [priority](priority.md) | [jvm]<br>val [priority](priority.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
