---
title: Lifecycle
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.lifecycle](../index.html)/[Lifecycle](index.html)



# Lifecycle



[jvm]\
data class [Lifecycle](index.html)(priority: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), listener: [PluginLifecycle](../index.html#1671498386%2FClasslikes%2F863300109)) : [Comparable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparable/index.html)&lt;[Lifecycle](index.html)&gt; 

Holds a lifecycle listener class and its priority



## Constructors


| | |
|---|---|
| [Lifecycle](-lifecycle.html) | [jvm]<br>fun [Lifecycle](-lifecycle.html)(priority: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), listener: [PluginLifecycle](../index.html#1671498386%2FClasslikes%2F863300109)) |


## Functions


| Name | Summary |
|---|---|
| [compareTo](compare-to.html) | [jvm]<br>open operator override fun [compareTo](compare-to.html)(other: [Lifecycle](index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |


## Properties


| Name | Summary |
|---|---|
| [listener](listener.html) | [jvm]<br>val [listener](listener.html): [PluginLifecycle](../index.html#1671498386%2FClasslikes%2F863300109) |
| [priority](priority.html) | [jvm]<br>val [priority](priority.html): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

