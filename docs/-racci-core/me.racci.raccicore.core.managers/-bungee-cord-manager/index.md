//[RacciCore](../../../index.md)/[me.racci.raccicore.core.managers](../index.md)/[BungeeCordManager](index.md)

# BungeeCordManager

[jvm]\
class [BungeeCordManager](index.md)(plugin: [RacciCore](../../me.racci.raccicore.core/-racci-core/index.md)) : PluginMessageListener, [LifecycleListener](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.md)
&lt;[RacciCore](../../me.racci.raccicore.core/-racci-core/index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [addToQueue](add-to-queue.md) | [jvm]<br>fun [addToQueue](add-to-queue.md)(request: [BungeeCordUtils.BungeeCordRequest](../../me.racci.raccicore.api.utils.minecraft/-bungee-cord-utils/-bungee-cord-request/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.md) | [jvm]<br>open suspend operator override fun [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.md)(event: [LifecycleEvent](../../me.racci.raccicore.api.lifecycle/-lifecycle-event/index.md))<br>Handles invoking the [Lifecycle](../../me.racci.raccicore.api.lifecycle/-lifecycle/index.md) |
| [onDisable](on-disable.md) | [jvm]<br>open suspend override fun [onDisable](on-disable.md)()<br>Called when the plugin is disabled. |
| [onEnable](on-enable.md) | [jvm]<br>open suspend override fun [onEnable](on-enable.md)()<br>Called when the RacciPlugin is enabled by the server and ready. |
| [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.md) | [jvm]<br>open suspend fun [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.md)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md) begins early loading. |
| [onPluginMessageReceived](on-plugin-message-received.md) | [jvm]<br>open override fun [onPluginMessageReceived](on-plugin-message-received.md)(channel: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), player: Player, message: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) |
| [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.md) | [jvm]<br>open suspend fun [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.md)()<br>Called when reloading the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md). |
| [sendBungeeCord](send-bungee-cord.md) | [jvm]<br>fun [sendBungeeCord](send-bungee-cord.md)(player: Player, message: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [plugin](plugin.md) | [jvm]<br>open override val [plugin](plugin.md): [RacciCore](../../me.racci.raccicore.core/-racci-core/index.md) |
