---
title: PlayerManager
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.core.managers](../index.html)/[PlayerManager](index.html)



# PlayerManager



[jvm]\
class [PlayerManager](index.html)(plugin: [RacciCore](../../me.racci.raccicore.core/-racci-core/index.html)) : [LifecycleListener](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.html)&lt;[RacciCore](../../me.racci.raccicore.core/-racci-core/index.html)&gt; , [KListener](../../me.racci.raccicore.api.extensions/-k-listener/index.html)&lt;[RacciCore](../../me.racci.raccicore.core/-racci-core/index.html)&gt;



## Types


| Name | Summary |
|---|---|
| [Companion](-companion/index.html) | [jvm]<br>object [Companion](-companion/index.html) |


## Functions


| Name | Summary |
|---|---|
| [get](get.html) | [jvm]<br>operator fun [get](get.html)(uuid: [UUID](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html)): [PlayerData](../../me.racci.raccicore.core.data/-player-data/index.html) |
| [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.html) | [jvm]<br>open suspend operator override fun [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.html)(event: [LifecycleEvent](../../me.racci.raccicore.api.lifecycle/-lifecycle-event/index.html))<br>Handles invoking the [Lifecycle](../../me.racci.raccicore.api.lifecycle/-lifecycle/index.html) |
| [onDisable](on-disable.html) | [jvm]<br>open suspend override fun [onDisable](on-disable.html)()<br>Called when the plugin is disabled. |
| [onEnable](on-enable.html) | [jvm]<br>open suspend override fun [onEnable](on-enable.html)()<br>Called when the RacciPlugin is enabled by the server and ready. |
| [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.html) | [jvm]<br>open suspend fun [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.html)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) begins early loading. |
| [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.html) | [jvm]<br>open suspend fun [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.html)()<br>Called when reloading the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html). |


## Properties


| Name | Summary |
|---|---|
| [functionsMove](functions-move.html) | [jvm]<br>val [functionsMove](functions-move.html): [OnlinePlayerMap](../../me.racci.raccicore.api.utils.collections/-online-player-map/index.html)&lt;[PlayerUtils.PlayerCallback](../../me.racci.raccicore.api.utils.minecraft/-player-utils/-player-callback/index.html)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt;&gt; |
| [functionsQuit](functions-quit.html) | [jvm]<br>val [functionsQuit](functions-quit.html): [OnlinePlayerMap](../../me.racci.raccicore.api.utils.collections/-online-player-map/index.html)&lt;[PlayerUtils.PlayerCallback](../../me.racci.raccicore.api.utils.minecraft/-player-utils/-player-callback/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;&gt; |
| [inputCallbacks](input-callbacks.html) | [jvm]<br>val [inputCallbacks](input-callbacks.html): [OnlinePlayerMap](../../me.racci.raccicore.api.utils.collections/-online-player-map/index.html)&lt;[PlayerUtils.ChatInput](../../me.racci.raccicore.api.utils.minecraft/-player-utils/-chat-input/index.html)&gt; |
| [plugin](plugin.html) | [jvm]<br>open override val [plugin](plugin.html): [RacciCore](../../me.racci.raccicore.core/-racci-core/index.html) |

