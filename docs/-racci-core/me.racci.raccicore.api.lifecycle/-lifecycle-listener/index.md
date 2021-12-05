---
title: LifecycleListener
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.api.lifecycle](../index.html)/[LifecycleListener](index.html)



# LifecycleListener



[jvm]\
interface [LifecycleListener](index.html)&lt;[P](index.html) : [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html)&gt; : [SuspendFunction1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-suspend-function1/index.html)&lt;[LifecycleEvent](../-lifecycle-event/index.html), [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt; , [WithPlugin](../../me.racci.raccicore.api.extensions/-with-plugin/index.html)&lt;[P](index.html)&gt;



## Functions


| Name | Summary |
|---|---|
| [invoke](invoke.html) | [jvm]<br>open suspend operator override fun [invoke](invoke.html)(event: [LifecycleEvent](../-lifecycle-event/index.html))<br>Handles invoking the [Lifecycle](../-lifecycle/index.html) |
| [onDisable](on-disable.html) | [jvm]<br>open suspend fun [onDisable](on-disable.html)()<br>Called when the plugin is disabled. |
| [onEnable](on-enable.html) | [jvm]<br>open suspend fun [onEnable](on-enable.html)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) is enabled by the server and ready. |
| [onLoad](on-load.html) | [jvm]<br>open suspend fun [onLoad](on-load.html)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) begins early loading. |
| [onReload](on-reload.html) | [jvm]<br>open suspend fun [onReload](on-reload.html)()<br>Called when reloading the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html). |


## Properties


| Name | Summary |
|---|---|
| [plugin](../../me.racci.raccicore.api.extensions/-with-plugin/plugin.html) | [jvm]<br>abstract val [plugin](../../me.racci.raccicore.api.extensions/-with-plugin/plugin.html): [P](index.html) |


## Inheritors


| Name |
|---|
| [PluginManager](../../me.racci.raccicore.core/-plugin-manager/index.html) |
| [BungeeCordManager](../../me.racci.raccicore.core.managers/-bungee-cord-manager/index.html) |
| [CommandManager](../../me.racci.raccicore.core.managers/-command-manager/index.html) |
| [HookManager](../../me.racci.raccicore.core.managers/-hook-manager/index.html) |
| [PlayerManager](../../me.racci.raccicore.core.managers/-player-manager/index.html) |

