---
title: BungeeCordManager
---
//[RacciCore](../../../index.html)/[me.racci.raccicore.core.managers](../index.html)/[BungeeCordManager](index.html)



# BungeeCordManager



[jvm]\
class [BungeeCordManager](index.html)(plugin: [RacciCore](../../me.racci.raccicore.core/-racci-core/index.html)) : PluginMessageListener, [LifecycleListener](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.html)&lt;[RacciCore](../../me.racci.raccicore.core/-racci-core/index.html)&gt;



## Types


| Name | Summary |
|---|---|
| [Companion](-companion/index.html) | [jvm]<br>object [Companion](-companion/index.html) |


## Functions


| Name | Summary |
|---|---|
| [addToQueue](add-to-queue.html) | [jvm]<br>fun [addToQueue](add-to-queue.html)(request: [BungeeCordUtils.BungeeCordRequest](../../me.racci.raccicore.api.utils.minecraft/-bungee-cord-utils/-bungee-cord-request/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.html) | [jvm]<br>open suspend operator override fun [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.html)(event: [LifecycleEvent](../../me.racci.raccicore.api.lifecycle/-lifecycle-event/index.html))<br>Handles invoking the [Lifecycle](../../me.racci.raccicore.api.lifecycle/-lifecycle/index.html) |
| [onDisable](on-disable.html) | [jvm]<br>open suspend override fun [onDisable](on-disable.html)()<br>Called when the plugin is disabled. |
| [onEnable](on-enable.html) | [jvm]<br>open suspend override fun [onEnable](on-enable.html)()<br>Called when the RacciPlugin is enabled by the server and ready. |
| [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.html) | [jvm]<br>open suspend fun [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.html)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html) begins early loading. |
| [onPluginMessageReceived](on-plugin-message-received.html) | [jvm]<br>open override fun [onPluginMessageReceived](on-plugin-message-received.html)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), player: Player, message: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) |
| [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.html) | [jvm]<br>open suspend fun [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.html)()<br>Called when reloading the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.html). |
| [sendBungeeCord](send-bungee-cord.html) | [jvm]<br>fun [sendBungeeCord](send-bungee-cord.html)(player: Player, message: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) |


## Properties


| Name | Summary |
|---|---|
| [plugin](plugin.html) | [jvm]<br>open override val [plugin](plugin.html): [RacciCore](../../me.racci.raccicore.core/-racci-core/index.html) |

