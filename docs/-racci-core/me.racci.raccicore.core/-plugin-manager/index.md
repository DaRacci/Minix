---
title: PluginManager
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.core](../index.html)/[PluginManager](index.html)



# PluginManager



[jvm]\
class [PluginManager](index.html)(plugin: [RacciCore](../-racci-core/index.html)) : [LifecycleListener](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.html)&lt;[RacciCore](../-racci-core/index.html)&gt; , [KListener](../../me.racci.raccicore.api.extensions/-k-listener/index.html)&lt;[RacciCore](../-racci-core/index.html)&gt;



## Functions


| Name | Summary |
|---|---|
| [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.html) | [jvm]<br>open suspend operator override fun [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.html)(event: [LifecycleEvent](../../me.racci.raccicore.api.lifecycle/-lifecycle-event/index.html))<br>Handles invoking the [Lifecycle](../../me.racci.raccicore.api.lifecycle/-lifecycle/index.html) |
| [onDisable](on-disable.html) | [jvm]<br>open suspend override fun [onDisable](on-disable.html)()<br>Called when the plugin is disabled. |
| [onEnable](on-enable.html) | [jvm]<br>open suspend override fun [onEnable](on-enable.html)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) is enabled by the server and ready. |
| [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.html) | [jvm]<br>open suspend fun [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.html)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) begins early loading. |
| [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.html) | [jvm]<br>open suspend fun [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.html)()<br>Called when reloading the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html). |


## Properties


| Name | Summary |
|---|---|
| [plugin](plugin.html) | [jvm]<br>open override val [plugin](plugin.html): [RacciCore](../-racci-core/index.html) |

