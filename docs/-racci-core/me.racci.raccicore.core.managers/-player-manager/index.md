//[RacciCore](../../../index.md)/[me.racci.raccicore.core.managers](../index.md)/[PlayerManager](index.md)

# PlayerManager

[jvm]\
class [PlayerManager](index.md)(plugin: [RacciCore](../../me.racci.raccicore.core/-racci-core/index.md)) : [LifecycleListener](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/index.md)&lt;[RacciCore](../../me.racci.raccicore.core/-racci-core/index.md)&gt;
, [KListener](../../me.racci.raccicore.api.extensions/-k-listener/index.md)&lt;[RacciCore](../../me.racci.raccicore.core/-racci-core/index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [get](get.md) | [jvm]<br>operator fun [get](get.md)(uuid: [UUID](https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html)): [PlayerData](../../me.racci.raccicore.core.data/-player-data/index.md) |
| [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.md) | [jvm]<br>open suspend operator override fun [invoke](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/invoke.md)(event: [LifecycleEvent](../../me.racci.raccicore.api.lifecycle/-lifecycle-event/index.md))<br>Handles invoking the [Lifecycle](../../me.racci.raccicore.api.lifecycle/-lifecycle/index.md) |
| [onDisable](on-disable.md) | [jvm]<br>open suspend override fun [onDisable](on-disable.md)()<br>Called when the plugin is disabled. |
| [onEnable](on-enable.md) | [jvm]<br>open suspend override fun [onEnable](on-enable.md)()<br>Called when the RacciPlugin is enabled by the server and ready. |
| [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.md) | [jvm]<br>open suspend fun [onLoad](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-load.md)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md) begins early loading. |
| [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.md) | [jvm]<br>open suspend fun [onReload](../../me.racci.raccicore.api.lifecycle/-lifecycle-listener/on-reload.md)()<br>Called when reloading the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md). |

## Properties

| Name | Summary |
|---|---|
| [functionsMove](functions-move.md) | [jvm]<br>val [functionsMove](functions-move.md): [OnlinePlayerMap](../../me.racci.raccicore.api.utils.collections/-online-player-map/index.md)&lt;[PlayerUtils.PlayerCallback](../../me.racci.raccicore.api.utils.minecraft/-player-utils/-player-callback/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt;&gt; |
| [functionsQuit](functions-quit.md) | [jvm]<br>val [functionsQuit](functions-quit.md): [OnlinePlayerMap](../../me.racci.raccicore.api.utils.collections/-online-player-map/index.md)&lt;[PlayerUtils.PlayerCallback](../../me.racci.raccicore.api.utils.minecraft/-player-utils/-player-callback/index.md)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;&gt; |
| [inputCallbacks](input-callbacks.md) | [jvm]<br>val [inputCallbacks](input-callbacks.md): [OnlinePlayerMap](../../me.racci.raccicore.api.utils.collections/-online-player-map/index.md)&lt;[PlayerUtils.ChatInput](../../me.racci.raccicore.api.utils.minecraft/-player-utils/-chat-input/index.md)&gt; |
| [plugin](plugin.md) | [jvm]<br>open override val [plugin](plugin.md): [RacciCore](../../me.racci.raccicore.core/-racci-core/index.md) |
