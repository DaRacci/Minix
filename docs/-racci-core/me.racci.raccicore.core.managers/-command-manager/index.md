---
title: CommandManager
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.core.managers](../index.html)/[CommandManager](index.html)



# CommandManager



[jvm]\
class [CommandManager](index.html)(plugin: [RacciCore](../../me.racci.raccicore.core/-racci-core/index.html)) : [KListener](../../me.racci.raccicore.api.extensions/-k-listener/index.html)&lt;[RacciCore](../../me.racci.raccicore.core/-racci-core/index.html)&gt; , [LifecycleListener](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.html)&lt;[RacciCore](../../me.racci.raccicore.core/-racci-core/index.html)&gt;



## Types


| Name | Summary |
|---|---|
| [Companion](-companion/index.html) | [jvm]<br>object [Companion](-companion/index.html) |


## Functions


| Name | Summary |
|---|---|
| [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.html) | [jvm]<br>open suspend operator override fun [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.html)(event: [LifecycleEvent](../../me.racci.raccicore.api.lifecycle/-lifecycle-event/index.html))<br>Handles invoking the [Lifecycle](../../me.racci.raccicore.api.lifecycle/-lifecycle/index.html) |
| [onDisable](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-disable.html) | [jvm]<br>open suspend fun [onDisable](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-disable.html)()<br>Called when the plugin is disabled. |
| [onEnable](on-enable.html) | [jvm]<br>open suspend override fun [onEnable](on-enable.html)()<br>Called when the RacciPlugin is enabled by the server and ready. |
| [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.html) | [jvm]<br>open suspend fun [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.html)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) begins early loading. |
| [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.html) | [jvm]<br>open suspend fun [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.html)()<br>Called when reloading the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html). |


## Properties


| Name | Summary |
|---|---|
| [commands](commands.html) | [jvm]<br>val [commands](commands.html): [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;Command&gt;&gt; |
| [plugin](plugin.html) | [jvm]<br>open override val [plugin](plugin.html): [RacciCore](../../me.racci.raccicore.core/-racci-core/index.html) |

