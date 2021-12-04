//[RacciCore](../../../index.md)/[me.racci.raccicore.api.lifecycle](../index.md)/[LifecycleListener](index.md)

# LifecycleListener

[jvm]\
interface [LifecycleListener](index.md)&lt;[P](index.md) : [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md)&gt; : [SuspendFunction1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-suspend-function1/index.html)&lt;[LifecycleEvent](
../-lifecycle-event/index.md), [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt; , [WithPlugin](../../me.racci.raccicore.api.extensions/-with-plugin/index.md)&lt;[P](index.md)&gt;

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [jvm]<br>open suspend operator override fun [invoke](invoke.md)(event: [LifecycleEvent](../-lifecycle-event/index.md))<br>Handles invoking the [Lifecycle](../-lifecycle/index.md) |
| [onDisable](on-disable.md) | [jvm]<br>open suspend fun [onDisable](on-disable.md)()<br>Called when the plugin is disabled. |
| [onEnable](on-enable.md) | [jvm]<br>open suspend fun [onEnable](on-enable.md)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md) is enabled by the server and ready. |
| [onLoad](on-load.md) | [jvm]<br>open suspend fun [onLoad](on-load.md)()<br>Called when the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md) begins early loading. |
| [onReload](on-reload.md) | [jvm]<br>open suspend fun [onReload](on-reload.md)()<br>Called when reloading the [RacciPlugin](../../me.racci.raccicore.api.plugin/-racci-plugin/index.md). |

## Properties

| Name | Summary |
|---|---|
| [plugin](../../me.racci.raccicore.api.extensions/-with-plugin/plugin.md) | [jvm]<br>abstract val [plugin](../../me.racci.raccicore.api.extensions/-with-plugin/plugin.md): [P](index.md) |

## Inheritors

| Name |
|---|
| [PluginManager](../../me.racci.raccicore.core/-plugin-manager/index.md) |
| [BungeeCordManager](../../me.racci.raccicore.core.managers/-bungee-cord-manager/index.md) |
| [CommandManager](../../me.racci.raccicore.core.managers/-command-manager/index.md) |
| [HookManager](../../me.racci.raccicore.core.managers/-hook-manager/index.md) |
| [PlayerManager](../../me.racci.raccicore.core.managers/-player-manager/index.md) |
